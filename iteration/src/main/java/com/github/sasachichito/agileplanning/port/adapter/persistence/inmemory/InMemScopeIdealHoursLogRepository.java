package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLogRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemScopeIdealHoursLogRepository implements ScopeIdealHoursLogRepository {

    private HashMap<PlanId, List<ScopeIdealHoursLog>> scopeChangeLogMap = new HashMap<>();

    @Override
    public List<ScopeIdealHoursLog> get(PlanId planId) {
        return this.scopeChangeLogMap.getOrDefault(planId, List.of());
    }

    @Override
    public List<ScopeIdealHoursLog> getAll() {
        return this.scopeChangeLogMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLog(ScopeIdealHoursLog scopeIdealHoursLog) {
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
