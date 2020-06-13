package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
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

    public TaskMilestoneList generateTaskMilestone(Plan plan) {
        WorkingHoursIncrement workingHoursIncrement = this.resourceRepository.get(plan.resourceId())
                .increasedWorkingHours(plan.period());

        return this.increasedStoryList(plan.scopeId())
                .taskMilestone(workingHoursIncrement);
    }

    public StoryMilestoneList generateStoryMilestone(Plan plan) {
        WorkingHoursIncrement workingHoursIncrement = this.resourceRepository.get(plan.resourceId())
                .increasedWorkingHours(plan.period());

        return this.increasedStoryList(plan.scopeId())
                .storyMilestone(workingHoursIncrement);
    }

    private StoryIncrementList increasedStoryList(ScopeId scopeId) {
        Scope scope = this.scopeRepository.get(scopeId);

        ControlRateForStory controlRateForStory = scope.controlRateForStory(
                new ControlRateForStoryCalculator(this.storyRepository),
                scope.idealHours(new ScopeIdealHoursCalculator(this.storyRepository)));

        return scope.increasedStoryList(
                new StoryIncrementGenerator(this.storyRepository),
                controlRateForStory);
    }
}
