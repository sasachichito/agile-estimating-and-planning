package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class UnitCostPerDay {
    @Getter
    private BigDecimal price;

    UnitCostPerDay(BigDecimal price) {
        this.price = price;
    }
}
