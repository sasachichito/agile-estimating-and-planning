package com.github.sasachichito.agileplanning.domain.model.burn;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class TotalBurnedHours {
    private BigDecimal hours;

    public TotalBurnedHours(BigDecimal hours) {
        this.hours = hours;
    }
}
