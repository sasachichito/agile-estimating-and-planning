package com.github.sasachichito.agileplanning.domain.model.scope;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class ControlRate {
    private BigDecimal rate;

    public ControlRate(BigDecimal rate) {
        this.rate = rate;
    }
}
