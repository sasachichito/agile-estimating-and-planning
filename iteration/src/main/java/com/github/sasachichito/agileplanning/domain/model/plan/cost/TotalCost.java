package com.github.sasachichito.agileplanning.domain.model.plan.cost;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class TotalCost {

    @Getter
    private BigDecimal totalCost;

    public TotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void merge(TotalCost merged) {
        this.totalCost = this.totalCost.add(merged.totalCost);
    }
}
