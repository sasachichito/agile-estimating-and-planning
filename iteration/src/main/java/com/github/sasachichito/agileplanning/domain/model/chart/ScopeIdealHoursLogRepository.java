package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;

import java.util.List;

public interface ScopeIdealHoursLogRepository {
    List<ScopeIdealHoursLog> get(PlanId planId);
    List<ScopeIdealHoursLog> getAll();
    void saveLog(ScopeIdealHoursLog scopeIdealHoursLog);
    void flash();
}
