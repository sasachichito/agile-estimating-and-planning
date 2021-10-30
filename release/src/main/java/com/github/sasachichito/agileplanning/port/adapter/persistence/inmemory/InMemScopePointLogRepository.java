package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLogRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemScopePointLogRepository implements ScopePointLogRepository {

    private HashMap<PlanId, List<ScopePointLog>> scopeChangeLogMap = new HashMap<>();

    @Override
    public List<ScopePointLog> get(PlanId planId) {
        return this.scopeChangeLogMap.getOrDefault(planId, List.of());
    }

    @Override
    public List<ScopePointLog> getAll() {
        return this.scopeChangeLogMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLog(ScopePointLog scopeIdealHoursLog) {
        this.scopeChangeLogMap.computeIfAbsent(
                scopeIdealHoursLog.planId(),
                (planId) -> new ArrayList<>());

        this.scopeChangeLogMap.get(scopeIdealHoursLog.planId())
                .add(scopeIdealHoursLog);
    }

    @Override
    public void flash() {
        this.scopeChangeLogMap = new HashMap<>();
    }
}
