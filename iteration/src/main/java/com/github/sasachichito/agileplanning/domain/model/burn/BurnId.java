package com.github.sasachichito.agileplanning.domain.model.burn;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
public class BurnId {
    private int id;

    public BurnId(int id) {
        this.id = id;
    }
}
