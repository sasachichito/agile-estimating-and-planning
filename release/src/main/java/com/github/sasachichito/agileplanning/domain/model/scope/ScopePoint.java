package com.github.sasachichito.agileplanning.domain.model.scope;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class ScopePoint {
    @Getter
    private BigDecimal point;

    public ScopePoint(BigDecimal point) {
        this.point = point;
    }
}
