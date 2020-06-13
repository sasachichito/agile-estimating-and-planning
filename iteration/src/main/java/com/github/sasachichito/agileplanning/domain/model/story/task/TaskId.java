package com.github.sasachichito.agileplanning.domain.model.story.task;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
public class TaskId {

    private int id;

    public TaskId(int id) {
        this.id = id;
    }
}
