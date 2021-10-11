package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
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
    private Integer version;
    private LocalDateTime updatedDateTime;
    private ScopeIdealHours scopeIdealHours;
    private List<LocalDate> period;
    private List<BigDecimal> changedPlan;
    private List<BigDecimal> actualResult;
    private String comment;

    public BurndownLineChart(
            PlanId planId,
            Integer version,
            LocalDateTime updatedDateTime,
            ScopeIdealHours scopeIdealHours,
            List<LocalDate> period,
            List<BigDecimal> changedPlan
    ) {
        this.planId = planId;
        this.version = version;
        this.updatedDateTime = updatedDateTime;
        this.period = period;
        this.changedPlan = changedPlan;
        this.scopeIdealHours = scopeIdealHours;
    }

    public BurndownLineChart(
            PlanId planId,
            Integer version,
            LocalDateTime updatedDateTime,
            ScopeIdealHours scopeIdealHours,
            List<LocalDate> period,
            List<BigDecimal> changedPlan,
            String comment
    ) {
        this.planId = planId;
        this.version = version;
        this.updatedDateTime = updatedDateTime;
        this.period = period;
        this.changedPlan = changedPlan;
        this.scopeIdealHours = scopeIdealHours;
        this.comment = comment;
    }

    public boolean isSameChart(BurndownLineChart aChart) {
        return this.planId.equals(aChart.planId)
                && this.scopeIdealHours.equals(aChart.scopeIdealHours)
                && this.period.equals(aChart.period)
                && this.changedPlan.equals(aChart.changedPlan);
    }

    public void setActualResult(List<BigDecimal> actualResult) { this.actualResult = actualResult; }
    public void setComment(String comment) {
        this.comment = comment;
    }
}

