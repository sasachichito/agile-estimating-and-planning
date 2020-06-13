package com.github.sasachichito.agileplanning.domain.model.scope;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
public class ScopeId {
    @Getter
    private int id;

    public ScopeId(int id) {
        this.id = id;
    }
}
