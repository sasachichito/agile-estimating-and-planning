package com.github.sasachichito.agileplanning.domain.model.story;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
@Getter
public class StoryId {
    private int id;

    public StoryId(int id) {
        this.id = id;
    }
}
