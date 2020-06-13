package com.github.sasachichito.agileplanning.domain.model.story;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class AdjustedStoryIdealHours {
    private BigDecimal hours;

    public AdjustedStoryIdealHours(BigDecimal hours) {
        this.hours = hours;
    }
}
