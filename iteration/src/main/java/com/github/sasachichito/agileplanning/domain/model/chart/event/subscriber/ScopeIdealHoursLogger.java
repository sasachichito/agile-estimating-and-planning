package com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHoursCalculator;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.time.LocalDateTime;

public class ScopeIdealHoursLogger implements ScopeChanged.Subscriber, PlanCreated.Subscriber {
    private static final ScopeIdealHoursLogger SCOPE_CHANGE_LOGGER = new ScopeIdealHoursLogger();

    public static ScopeIdealHoursLogger instance() {
        return SCOPE_CHANGE_LOGGER;
    }

    private ScopeIdealHoursLogger() {}

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
        ScopeIdealHours scopeIdealHours = scopeChanged.scope().idealHours(
                new ScopeIdealHoursCalculator(this.storyRepository));

        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasScope(scopeChanged.scope().scopeId()))
                .forEach(plan -> {
                    ScopeIdealHoursLog scopeIdealHoursLog = new ScopeIdealHoursLog(
                            plan.planId(),
                            LocalDateTime.now(),
                            scopeIdealHours,
                            ScopeIdealHoursLog.ChangeType.NEW_STORY);

                    this.burndownChartService.save(scopeIdealHoursLog);
                });
    }

    @Override
    public void handle(PlanCreated planCreated) {
        Scope scope = this.scopeRepository.get(planCreated.plan().scopeId());

        this.burndownChartService.save(
                new ScopeIdealHoursLog(
                        planCreated.plan().planId(),
                        LocalDateTime.now(),
                        scope.idealHours(
                                new ScopeIdealHoursCalculator(this.storyRepository)),
                        ScopeIdealHoursLog.ChangeType.INITIAL
                )
        );
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }
}
