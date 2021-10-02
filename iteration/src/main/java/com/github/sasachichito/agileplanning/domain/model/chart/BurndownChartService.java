package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;

import java.util.List;

public interface BurndownChartService {
    List<ScopeIdealHoursLog> get(PlanId planId);
    List<ScopeIdealHoursLog> getAll();
    void saveLog(ScopeIdealHoursLog scopeIdealHoursLog);
    void saveChart(BurndownLineChart burndownLineChart);
    BurndownLineChart makeLineChart(Plan plan, Resource resource, ScopeIdealHours scopeIdealHours);
    BurndownLineChart getLineChart(Plan plan, Resource resource, BurnIncrement burnIncrement);
    BurndownLineChartList getLineCharts(PlanId planId);
    void flash();
}
