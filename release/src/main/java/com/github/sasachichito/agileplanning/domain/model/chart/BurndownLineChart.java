package com.github.sasachichito.agileplanning.domain.model.chart;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Accessors(fluent = true)
@Getter
public class BurndownLineChart {
    private List<LocalDate> period;
    private List<BigDecimal> initialPlan;
    private List<BigDecimal> changedPlan;
    private List<BigDecimal> actualResult;

    public BurndownLineChart(List<LocalDate> period, List<BigDecimal> initialPlan, List<BigDecimal> changedPlan, List<BigDecimal> actualResult) {
        this.period = period;
        this.initialPlan = initialPlan;
        this.changedPlan = changedPlan;
        this.actualResult = actualResult;
    }
}
