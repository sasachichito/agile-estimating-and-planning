package com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLogRepository;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePointCalculator;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.time.LocalDateTime;

public class ScopePointLogger implements
        ScopeChanged.Subscriber,
        ResourceChanged.Subscriber,
        PlanCreated.Subscriber {

    private static final ScopePointLogger SCOPE_CHANGE_LOGGER = new ScopePointLogger();

    public static ScopePointLogger instance() {
        return SCOPE_CHANGE_LOGGER;
    }

    private ScopePointLogger() {}

    private StoryRepository storyRepository;
    private PlanRepository planRepository;
    private ScopeRepository scopeRepository;
    private ScopePointLogRepository scopePointLogRepository;
    private BurndownChartService burndownChartService;

    public void init(
            StoryRepository storyRepository,
            PlanRepository planRepository,
            ScopeRepository scopeRepository,
            ScopePointLogRepository scopePointLogRepository,
            BurndownChartService burndownChartService
    ) {
        this.storyRepository = storyRepository;
        this.planRepository = planRepository;
        this.scopeRepository = scopeRepository;
        this.scopePointLogRepository = scopePointLogRepository;
        this.burndownChartService = burndownChartService;
    }

    @Override
    public void handle(ScopeChanged scopeChanged) {
        ScopePoint scopePoint = scopeChanged.scope().scopePoint(
                new ScopePointCalculator(this.storyRepository));

        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasScope(scopeChanged.scope().scopeId()))
                .forEach(plan -> {
                    ScopePointLog scopePointLog = new ScopePointLog(
                            plan.planId(),
                            LocalDateTime.now(),
                            scopePoint,
                            ScopePointLog.ChangeType.NEW_STORY);

                    this.scopePointLogRepository.saveLog(scopePointLog);

                    this.burndownChartService.saveChart(plan, scopePoint);
                });
    }

    @Override
    public void handle(ResourceChanged resourceChanged) {
        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasResource(resourceChanged.resource().resourceId()))
                .forEach(plan -> {
                    ScopePoint scopePoint = scopeRepository.get(plan.scopeId()).scopePoint(
                            new ScopePointCalculator(this.storyRepository));

                    ScopePointLog scopePointLog = new ScopePointLog(
                            plan.planId(),
                            LocalDateTime.now(),
                            scopePoint,
                            ScopePointLog.ChangeType.RESOURCE_CHANGED);

                    this.scopePointLogRepository.saveLog(scopePointLog);

                    this.burndownChartService.saveChart(plan, scopePoint);
                });
    }

    @Override
    public void handle(PlanCreated planCreated) {
        ScopePoint scopePoint = this.scopeRepository.get(planCreated.plan().scopeId()).scopePoint(
                new ScopePointCalculator(this.storyRepository));

        this.scopePointLogRepository.saveLog(
                new ScopePointLog(
                        planCreated.plan().planId(),
                        LocalDateTime.now(),
                        scopePoint,
                        ScopePointLog.ChangeType.INITIAL
                )
        );

        this.burndownChartService.saveChart(planCreated.plan(), scopePoint);
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

}
