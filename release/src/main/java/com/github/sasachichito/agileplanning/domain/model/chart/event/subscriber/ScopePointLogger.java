package com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePointCalculator;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.time.LocalDateTime;

public class ScopePointLogger implements ScopeChanged.Subscriber, PlanCreated.Subscriber {

    private static final ScopePointLogger SCOPE_CHANGE_LOGGER = new ScopePointLogger();

    public static ScopePointLogger instance() {
        return SCOPE_CHANGE_LOGGER;
    }

    private ScopePointLogger() {}

    private StoryRepository storyRepository;
    private PlanRepository planRepository;
    private ScopeRepository scopeRepository;
    private BurndownChartService burndownChartService;

    public void init(
            StoryRepository storyRepository,
            PlanRepository planRepository,
            ScopeRepository scopeRepository,
            BurndownChartService burndownChartService
    ) {
        this.storyRepository = storyRepository;
        this.planRepository = planRepository;
        this.scopeRepository = scopeRepository;
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

                    this.burndownChartService.save(scopePointLog);
                });
    }

    @Override
    public void handle(PlanCreated planCreated) {
        Scope scope = this.scopeRepository.get(planCreated.plan().scopeId());

        this.burndownChartService.save(
                new ScopePointLog(
                        planCreated.plan().planId(),
                        LocalDateTime.now(),
                        scope.scopePoint(new ScopePointCalculator(this.storyRepository)),
                        ScopePointLog.ChangeType.INITIAL
                )
        );
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

}
