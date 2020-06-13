package com.github.sasachichito.agileplanning.domain.model.plan;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.period.PeriodCalculator;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

public class PlanFactory {

    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;
    private ResourceRepository resourceRepository;

    public PlanFactory(
            ScopeRepository scopeRepository,
            StoryRepository storyRepository,
            ResourceRepository resourceRepository
    ) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
        this.resourceRepository = resourceRepository;
    }

    public Plan create(PlanId planId, PlanTitle planTitle, ScopeId scopeId, ResourceId resourceId) {
        var periodCalculator = new PeriodCalculator(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        return new Plan(planId, planTitle, scopeId, resourceId,
                periodCalculator.exec(scopeId, resourceId));
    }

    public Plan create(PlanId planId, PlanTitle planTitle, ScopeId scopeId, ResourceId resourceId, Period period) {
        this.validate(
                this.scopeRepository.get(scopeId),
                this.resourceRepository.get(resourceId),
                period
        );

        return new Plan(planId, planTitle, scopeId, resourceId, period);
    }

    public void change(Plan plan, PlanTitle planTitle, ScopeId scopeId, ResourceId resourceId, Period period) {
        this.validate(
                this.scopeRepository.get(scopeId),
                this.resourceRepository.get(resourceId),
                period
        );

        plan.change(planTitle, scopeId, resourceId, period);
    }

    private void validate(Scope scope, Resource resource, Period period) {
        if (resource.canFinishUpIn(
                scope.scopePoint(new ScopePointCalculator(this.storyRepository)), period)
        ) {
            return;
        }
        throw new IllegalArgumentException("ScopeId " + scope.scopeId().id() + " の理想時間を燃焼しきれません。");
    }
}
