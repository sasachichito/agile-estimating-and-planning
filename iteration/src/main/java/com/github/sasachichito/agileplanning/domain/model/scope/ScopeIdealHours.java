package com.github.sasachichito.agileplanning.domain.model.scope;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode
@Accessors(fluent = true)
public class ScopeIdealHours {
    @Getter
    BigDecimal hours;

    public ScopeIdealHours(BigDecimal hours) {
        this.hours = hours;
    }
}
