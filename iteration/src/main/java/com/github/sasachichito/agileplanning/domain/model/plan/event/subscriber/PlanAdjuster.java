package com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.period.PeriodCalculator;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryChanged;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;

import java.util.Set;
import java.util.stream.Collectors;

public class PlanAdjuster implements
        ScopeChanged.Subscriber,
        ResourceChanged.Subscriber
{
    private static final PlanAdjuster PLAN_ADJUSTER = new PlanAdjuster();

    public static PlanAdjuster instance() {
        return PLAN_ADJUSTER;
    }

    private PlanAdjuster() {}

    private PlanRepository planRepository;
    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;
    private ResourceRepository resourceRepository;

    public void setRepositories(
            PlanRepository planRepository,
            ScopeRepository scopeRepository,
            StoryRepository storyRepository,
            ResourceRepository resourceRepository
    ) {
        this.planRepository = planRepository;
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(ScopeChanged scopeChanged) {
        Set<Plan> planSet = this.planRepository.getAll();
        planSet.stream()
                .filter(plan -> plan.hasScope(scopeChanged.scope().scopeId()))
                .forEach(plan -> {
                    plan.adjustPeriod(
                            new PeriodCalculator(this.scopeRepository, this.storyRepository, this.resourceRepository)
                    );
                    this.planRepository.put(plan);
                });
    }

    @Override
    public void handle(ResourceChanged resourceChanged) {
        Set<Plan> planSet = this.planRepository.getAll();
        planSet.stream()
                .filter(plan -> plan.hasResource(resourceChanged.resource().resourceId()))
                .forEach(plan -> {
                    plan.adjustPeriod(
                            new PeriodCalculator(this.scopeRepository, this.storyRepository, this.resourceRepository)
                    );
                    this.planRepository.put(plan);
                });
    }

//    @Override
//    public void handle(StoryChanged storyChanged) {
//        Set<ScopeId> scopeIdSet = this.scopeRepository.getAll().stream()
//                .filter(scope -> scope.hasStory(storyChanged.story().storyId()))
//                .map(Scope::scopeId)
//                .collect(Collectors.toSet());
//
//        Set<Plan> planSet = this.planRepository.getAll();
//
//        planSet.stream()
//                .filter(plan -> plan.hasScope(scopeIdSet))
//                .forEach(plan -> {
//                    plan.adjustPeriod(
//                            new PeriodCalculator(this.scopeRepository, this.storyRepository, this.resourceRepository)
//                    );
//                    this.planRepository.put(plan);
//                });
//    }
}
