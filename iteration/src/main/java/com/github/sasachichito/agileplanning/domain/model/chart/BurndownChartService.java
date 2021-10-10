package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;

public interface BurndownChartService {
    void saveChart(Plan plan, ScopeIdealHours scopeIdealHours);
    BurndownLineChartList getLineCharts(PlanId planId);
    BurndownLineChartList getLineCharts();
}
