package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(fluent = true)
@Getter
public class BurndownLineChart {
    private PlanId planId;
    private LocalDateTime localDateTime;
    private List<LocalDate> period;
    private List<BigDecimal> initialPlan;
    private List<BigDecimal> changedPlan;
    private List<BigDecimal> actualResult;
    private String comment;

    public BurndownLineChart(List<LocalDate> period, List<BigDecimal> initialPlan, List<BigDecimal> changedPlan, List<BigDecimal> actualResult) {
        this.period = period;
        this.initialPlan = initialPlan;
        this.changedPlan = changedPlan;
        this.actualResult = actualResult;
    }

    public BurndownLineChart(
            PlanId planId,
            LocalDateTime localDateTime,
            List<LocalDate> period,
            List<BigDecimal> initialPlan,
            List<BigDecimal> changedPlan,
            List<BigDecimal> actualResult
    ) {
        this.planId = planId;
        this.localDateTime = localDateTime;
        this.period = period;
        this.initialPlan = initialPlan;
        this.changedPlan = changedPlan;
        this.actualResult = actualResult;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

