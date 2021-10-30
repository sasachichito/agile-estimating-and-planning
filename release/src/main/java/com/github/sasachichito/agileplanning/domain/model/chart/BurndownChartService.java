package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;

import java.util.List;

public interface BurndownChartService {
    void saveChart(Plan plan, ScopePoint ScopePoint);
    BurndownLineChartList getLineCharts(PlanId planId);
    List<BurndownLineChart> getLineCharts();
}
