package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

public class MilestoneService {

    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;
    private ResourceRepository resourceRepository;

    public MilestoneService(
            ScopeRepository scopeRepository,
            StoryRepository storyRepository,
            ResourceRepository resourceRepository
    ) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
        this.resourceRepository = resourceRepository;
    }

    public MilestoneList generateMilestoneList(Plan plan) {
        VelocityIncrement velocityIncrement = this.resourceRepository.get(plan.resourceId())
                .storyPointIncrement(plan.period());

        return this.storyPointIncrementList(plan.scopeId())
                .milestoneList(velocityIncrement);
    }

    private StoryPointIncrementList storyPointIncrementList(ScopeId scopeId) {
        Scope scope = this.scopeRepository.get(scopeId);

        ControlRate controlRate = scope.controlRate(
                new ControlRateCalculator(this.storyRepository),
                scope.scopePoint(new ScopePointCalculator(this.storyRepository))
        );

        return scope.storyPointIncrementList(
                new StoryPointIncrementGenerator(this.storyRepository),
                controlRate);
    }
}
