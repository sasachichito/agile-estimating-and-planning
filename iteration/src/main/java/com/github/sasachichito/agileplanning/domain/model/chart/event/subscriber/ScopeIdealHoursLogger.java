package com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.burn.*;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHoursCalculator;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ScopeIdealHoursLogger implements
        ScopeChanged.Subscriber,
        ResourceChanged.Subscriber,
        PlanCreated.Subscriber {
    private static final ScopeIdealHoursLogger SCOPE_CHANGE_LOGGER = new ScopeIdealHoursLogger();

    public static ScopeIdealHoursLogger instance() {
        return SCOPE_CHANGE_LOGGER;
    }

    private ScopeIdealHoursLogger() {}

    private StoryRepository storyRepository;
    private ResourceRepository resourceRepository;
    private BurnRepository burnRepository;
    private PlanRepository planRepository;
    private ScopeRepository scopeRepository;
    private BurndownChartService burndownChartService;

    public void init(
            StoryRepository storyRepository,
            ResourceRepository resourceRepository,
            BurnRepository burnRepository,
            PlanRepository planRepository,
            ScopeRepository scopeRepository,
            BurndownChartService burndownChartService
    ) {
        this.storyRepository = storyRepository;
        this.resourceRepository = resourceRepository;
        this.burnRepository = burnRepository;
        this.planRepository = planRepository;
        this.scopeRepository = scopeRepository;
        this.burndownChartService = burndownChartService;
    }

    @Override
    public void handle(ScopeChanged scopeChanged) {
        ScopeIdealHours scopeIdealHours = scopeChanged.scope().idealHours(
                new ScopeIdealHoursCalculator(this.storyRepository));

        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasScope(scopeChanged.scope().scopeId()))
                .forEach(plan -> {
                    ScopeIdealHoursLog scopeIdealHoursLog = new ScopeIdealHoursLog(
                            plan.planId(),
                            LocalDateTime.now(),
                            scopeIdealHours,
                            ScopeIdealHoursLog.ChangeType.SCOPE_CHANGED);

                    this.burndownChartService.saveLog(scopeIdealHoursLog);

                    this.saveBurndownChart(plan, scopeIdealHours);
                });
    }

    @Override
    public void handle(ResourceChanged resourceChanged) {
        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasResource(resourceChanged.resource().resourceId()))
                .forEach(plan -> {
                    ScopeIdealHours scopeIdealHours = scopeRepository.get(plan.scopeId()).idealHours(
                            new ScopeIdealHoursCalculator(this.storyRepository));

                    ScopeIdealHoursLog scopeIdealHoursLog = new ScopeIdealHoursLog(
                            plan.planId(),
                            LocalDateTime.now(),
                            scopeIdealHours,
                            ScopeIdealHoursLog.ChangeType.RESOURCE_CHANGED);

                    this.burndownChartService.saveLog(scopeIdealHoursLog);

                    this.saveBurndownChart(plan, scopeIdealHours);
                });
    }

    @Override
    public void handle(PlanCreated planCreated) {
        ScopeIdealHours scopeIdealHours = scopeRepository.get(planCreated.plan().scopeId()).idealHours(
                new ScopeIdealHoursCalculator(this.storyRepository));

        this.burndownChartService.saveLog(
                new ScopeIdealHoursLog(
                        planCreated.plan().planId(),
                        LocalDateTime.now(),
                        scopeIdealHours,
                        ScopeIdealHoursLog.ChangeType.INITIAL
                ));

        this.saveBurndownChart(planCreated.plan(), scopeIdealHours);
    }

    private void saveBurndownChart(Plan plan, ScopeIdealHours scopeIdealHours) {
        var burndownLineChart = burndownChartService.makeLineChart(
                plan,
                this.resourceRepository.get(plan.resourceId()),
                scopeIdealHours);

        burndownChartService.saveChart(burndownLineChart);
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }
}
