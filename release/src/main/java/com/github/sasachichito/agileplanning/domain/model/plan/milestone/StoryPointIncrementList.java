package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;

import java.util.List;
import java.util.stream.Collectors;

public class StoryPointIncrementList {
    private List<StoryPointIncrement> storyPointIncrementList;

    public StoryPointIncrementList(List<StoryPointIncrement> storyPointIncrementList) {
        this.storyPointIncrementList = storyPointIncrementList;
    }

    public MilestoneList milestoneList(VelocityIncrement velocityIncrement) {
        List<Milestone> milestoneList = this.storyPointIncrementList.stream()
                .map(storyPointIncrement -> storyPointIncrement.milestone(velocityIncrement))
                .collect(Collectors.toList());

        return new MilestoneList(milestoneList);
    }
}
