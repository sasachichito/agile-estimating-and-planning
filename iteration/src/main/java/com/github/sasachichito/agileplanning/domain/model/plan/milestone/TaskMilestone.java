package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(fluent = true)
@Getter
public class TaskMilestone {
    private TaskAndRelatedStory taskAndRelatedStory;
    private LocalDate localDate;

    public TaskMilestone(TaskAndRelatedStory taskAndRelatedStory, LocalDate localDate) {
        this.taskAndRelatedStory = taskAndRelatedStory;
        this.localDate = localDate;
    }
}
