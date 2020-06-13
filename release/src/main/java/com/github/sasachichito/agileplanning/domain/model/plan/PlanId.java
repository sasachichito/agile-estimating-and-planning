package com.github.sasachichito.agileplanning.domain.model.plan;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@Accessors(fluent = true)
public class PlanId {
    @Getter
    private int id;

    public PlanId(int id) {
        this.id = id;
    }
}
