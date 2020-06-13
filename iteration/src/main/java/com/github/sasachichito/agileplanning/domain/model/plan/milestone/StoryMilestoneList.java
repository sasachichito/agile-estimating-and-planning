package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Getter
public class StoryMilestoneList {
    private List<StoryMilestone> storyMilestoneList;

    public StoryMilestoneList(List<StoryMilestone> storyMilestoneList) {
        this.storyMilestoneList = storyMilestoneList;
    }
}
