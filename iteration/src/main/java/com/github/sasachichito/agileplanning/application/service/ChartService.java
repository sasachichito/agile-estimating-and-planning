package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChartList;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChartService {
    private final BurndownChartService burndownChartService;

    public BurndownLineChartList burndownLineCharts(PlanId planId) {
        return this.burndownChartService.getLineCharts(planId);
    }

    public List<BurndownLineChart> burndownLineCharts() {
        return this.burndownChartService.getLineCharts();
    }
}
