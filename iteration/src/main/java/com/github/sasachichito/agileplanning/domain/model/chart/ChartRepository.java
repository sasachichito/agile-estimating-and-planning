package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;

import java.util.List;
import java.util.Map;

public interface ChartRepository {
    BurndownLineChart get(Map<PlanId, Integer> key);
    List<BurndownLineChart> getList(PlanId planId);
    BurndownLineChart getLastVersion(PlanId planId);
    List<BurndownLineChart> getAll();
    boolean exist(PlanId planId);
    void add(BurndownLineChart burndownLineChart);
    void flash();
}
