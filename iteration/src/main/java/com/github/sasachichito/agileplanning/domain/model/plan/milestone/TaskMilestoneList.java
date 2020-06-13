package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@Getter
public class TaskMilestoneList {
    private List<TaskMilestone> taskMilestoneList;

    public TaskMilestoneList(List<TaskMilestone> taskMilestoneList) {
        this.taskMilestoneList = taskMilestoneList;
    }

    public TaskMilestoneList filterRelate(Story story) {
        List<TaskMilestone> listRelateTheStory = this.taskMilestoneList.stream()
                .filter(taskMilestone -> taskMilestone.taskAndRelatedStory().story()
                        .equals(story))
                .collect(Collectors.toList());

        return new TaskMilestoneList(listRelateTheStory);
    }
}
