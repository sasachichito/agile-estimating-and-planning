package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;

import java.util.List;

public interface BurndownChartService {
    List<ScopePointLog> get(PlanId planId);
    List<ScopePointLog> getAll();
    void save(ScopePointLog scopePointLog);
    BurndownLineChart getLineChart(Plan plan, Resource resource, BurnIncrement burnIncrement);
    void flash();
}
