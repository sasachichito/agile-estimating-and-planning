package com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLogRepository;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHoursCalculator;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.time.LocalDateTime;

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
    private PlanRepository planRepository;
    private ScopeRepository scopeRepository;
    private ScopeIdealHoursLogRepository scopeIdealHoursLogRepository;
    private BurndownChartService burndownChartService;

    public void init(
            StoryRepository storyRepository,
            PlanRepository planRepository,
            ScopeRepository scopeRepository,
            ScopeIdealHoursLogRepository scopeIdealHoursLogRepository,
            BurndownChartService burndownChartService
    ) {
        this.storyRepository = storyRepository;
        this.planRepository = planRepository;
        this.scopeRepository = scopeRepository;
        this.scopeIdealHoursLogRepository = scopeIdealHoursLogRepository;
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

                    this.scopeIdealHoursLogRepository.saveLog(scopeIdealHoursLog);

                    this.burndownChartService.saveChart(plan, scopeIdealHours);
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

                    this.scopeIdealHoursLogRepository.saveLog(scopeIdealHoursLog);

                    this.burndownChartService.saveChart(plan, scopeIdealHours);
                });
    }

    @Override
    public void handle(PlanCreated planCreated) {
        ScopeIdealHours scopeIdealHours = scopeRepository.get(planCreated.plan().scopeId()).idealHours(
                new ScopeIdealHoursCalculator(this.storyRepository));

        this.scopeIdealHoursLogRepository.saveLog(
                new ScopeIdealHoursLog(
                        planCreated.plan().planId(),
                        LocalDateTime.now(),
                        scopeIdealHours,
                        ScopeIdealHoursLog.ChangeType.INITIAL
                ));

        this.burndownChartService.saveChart(planCreated.plan(), scopeIdealHours);
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }
}
