package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class TaskIncrement {
    private TaskAndRelatedStory taskAndRelatedStory;
    private IdealHoursIncrement idealHoursIncrement;

    public TaskIncrement(TaskAndRelatedStory taskAndRelatedStory, IdealHoursIncrement idealHoursIncrement) {
        this.taskAndRelatedStory = taskAndRelatedStory;
        this.idealHoursIncrement = idealHoursIncrement;
    }

    public TaskMilestone taskMilestone(WorkingHoursIncrement workingHoursIncrement) {
        return new TaskMilestone(
                this.taskAndRelatedStory,
                workingHoursIncrement.finishDate(this.idealHoursIncrement)
        );
    }
}
