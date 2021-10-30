package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.chart.ChartRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemChartRepository implements ChartRepository {
    private HashMap<Map<PlanId, Integer>, BurndownLineChart> burndownLineChartMap = new HashMap<>();

    @Override
    public BurndownLineChart get(Map<PlanId, Integer> key) {
        if (this.burndownLineChartMap.containsKey(key)) {
            return this.burndownLineChartMap.get(key);
        }

        if (key.keySet().stream().findFirst().isPresent() && key.values().stream().findFirst().isPresent()) {
            throw new ResourceNotFoundException(
                    "PlanId " + key.keySet().stream().findFirst().get().id() +
                            " Version " + key.values().stream().findFirst().get() +
                            " is not found"
            );
        }
        throw new IllegalArgumentException("PlanId, Versionが指定されていません.");
    }

    @Override
    public List<BurndownLineChart> getList(PlanId planId) {
        return this.burndownLineChartMap.entrySet().stream()
                .filter(entry -> entry.getKey().containsKey(planId))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public BurndownLineChart getLastVersion(PlanId planId) {
        return this.burndownLineChartMap.entrySet().stream()
                .filter(entry -> entry.getKey().containsKey(planId))
                .map(Map.Entry::getValue)
                .max(Comparator.comparing(BurndownLineChart::version))
                .orElseThrow(() -> new IllegalArgumentException("PlanId " + planId.id() + " のチャートは存在しません."));
    }

    @Override
    public List<BurndownLineChart> getAll() {
        return new ArrayList<>(this.burndownLineChartMap.values());
    }

    @Override
    public boolean exist(PlanId planId) {
        return this.burndownLineChartMap.entrySet().stream()
                .anyMatch(entry -> entry.getKey().containsKey(planId));
    }

    @Override
    public void add(BurndownLineChart burndownLineChart) {
        Map<PlanId, Integer> key = Map.of(burndownLineChart.planId(), burndownLineChart.version());
        this.burndownLineChartMap.put(key, burndownLineChart);
    }

    @Override
    public void flash() {
        this.burndownLineChartMap = new HashMap<>();
    }
}