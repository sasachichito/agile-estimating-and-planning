package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;

import java.util.List;

public interface ScopePointLogRepository {
    List<ScopePointLog> get(PlanId planId);
    List<ScopePointLog> getAll();
    void saveLog(ScopePointLog scopeIdealHoursLog);
    void flash();
}
