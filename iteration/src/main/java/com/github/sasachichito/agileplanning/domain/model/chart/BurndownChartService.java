package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;

import java.util.List;

public interface BurndownChartService {
    List<ScopeIdealHoursLog> get(PlanId planId);
    List<ScopeIdealHoursLog> getAll();
    void save(ScopeIdealHoursLog scopeIdealHoursLog);
    BurndownLineChart getLineChart(Plan plan, Resource resource, BurnIncrement burnIncrement);
    void flash();
}
