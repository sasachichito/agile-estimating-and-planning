package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class IdealHoursIncrement {
    @Getter
    private BigDecimal hours;

    public IdealHoursIncrement(BigDecimal hours) {
        this.hours = hours;
    }
}
