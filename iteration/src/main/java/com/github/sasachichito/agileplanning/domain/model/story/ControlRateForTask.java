package com.github.sasachichito.agileplanning.domain.model.story;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class ControlRateForTask {
    private BigDecimal rate;

    public ControlRateForTask(BigDecimal rate) {
        this.rate = rate;
    }
}
