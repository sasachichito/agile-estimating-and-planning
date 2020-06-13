package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.task.Task;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class TaskAndRelatedStory {
    private Task task;
    private Story story;

    public TaskAndRelatedStory(Task task, Story story) {
        this.task = task;
        this.story = story;
    }
}
