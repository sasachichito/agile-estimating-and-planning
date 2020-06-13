package com.github.sasachichito.agileplanning.domain.model.story.task;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Accessors(fluent = true)
@Getter
public class TaskName {

    private String name;

    public TaskName(String name) {
        this.setName(name);
    }

    private void setName(String name) {
        if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("TaskNameは必須です.");
        this.name = name;
    }
}
