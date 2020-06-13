package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StoryIncrementList {
    private List<StoryIncrement> storyIncrementList;

    public StoryIncrementList(List<StoryIncrement> storyIncrementList) {
        this.storyIncrementList = storyIncrementList;
    }

    public TaskMilestoneList taskMilestone(WorkingHoursIncrement workingHoursIncrement) {
        List<TaskMilestone> taskMilestoneList = this.storyIncrementList.stream()
                .map(increasedStory -> increasedStory.taskMilestone(workingHoursIncrement))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new TaskMilestoneList(taskMilestoneList);
    }

    public StoryMilestoneList storyMilestone(WorkingHoursIncrement workingHoursIncrement) {
        List<StoryMilestone> storyMilestoneList = this.storyIncrementList.stream()
                .map(increasedStory -> increasedStory.storyMilestone(workingHoursIncrement))
                .collect(Collectors.toList());

        return new StoryMilestoneList(storyMilestoneList);
    }
}
