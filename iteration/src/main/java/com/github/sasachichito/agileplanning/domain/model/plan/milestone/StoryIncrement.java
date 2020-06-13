package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.story.Story;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class StoryIncrement {
    private Story story;
    private List<TaskIncrement> taskIncrementList;

    public StoryIncrement(Story story, List<TaskIncrement> taskIncrementList) {
        this.story = story;
        this.taskIncrementList = taskIncrementList;
    }

    public BigDecimal lastHours() {
        return this.taskIncrementList.isEmpty()
                ? BigDecimal.ZERO
                : this.taskIncrementList.get(this.taskIncrementList.size() - 1).idealHoursIncrement().hours();
    }

    public List<TaskMilestone> taskMilestone(WorkingHoursIncrement workingHoursIncrement) {
        return this.taskIncrementList.stream()
                .map(taskIncrement -> taskIncrement.taskMilestone(workingHoursIncrement))
                .collect(Collectors.toList());
    }

    public StoryMilestone storyMilestone(WorkingHoursIncrement workingHoursIncrement) {
        if (this.taskIncrementList.isEmpty()) {
            return new StoryMilestone(
                    this.story,
                    workingHoursIncrement.finishDate(new IdealHoursIncrement(BigDecimal.ZERO)));
        }

        IdealHoursIncrement storyIdealHours = this.taskIncrementList
                .get(this.taskIncrementList.size() -1).idealHoursIncrement();

        return new StoryMilestone(
                this.taskIncrementList.get(0).taskAndRelatedStory().story(),
                workingHoursIncrement.finishDate(storyIdealHours));
    }
}
