package com.github.sasachichito.agileplanning.domain.model.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
public class ResourceId {
    @Getter
    private int id;

    public ResourceId(int id) {
        this.id = id;
    }
}
