package com.github.sasachichito.agileplanning.domain.model.story.task;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class AdjustedTaskIdealHours {
    private BigDecimal hours;

    public AdjustedTaskIdealHours(BigDecimal hours) {
        this.hours = hours;
    }
}
