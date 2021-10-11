package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChartList;
import com.github.sasachichito.agileplanning.domain.model.chart.ChartRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChartService {
    private final BurndownChartService burndownChartService;
    private final ChartRepository chartRepository;

    public BurndownLineChartList burndownLineCharts(PlanId planId) {
        return this.burndownChartService.getLineCharts(planId);
    }

    public BurndownLineChart addCommnet(PlanId planId, int version, String comment) {
        BurndownLineChart burndownLineChart = this.chartRepository.get(Map.of(planId, version));
        burndownLineChart.setComment(comment);
        this.chartRepository.add(burndownLineChart);
        return burndownLineChart;
    }

    public List<BurndownLineChart> burndownLineCharts() {
        return this.burndownChartService.getLineCharts();
    }
}
