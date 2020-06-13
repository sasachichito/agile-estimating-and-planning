package com.github.sasachichito.agileplanning.port.adapter.resource.chart.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JsonBurndownLineChart {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d");

    private List<String> period;
    private List<BigDecimal> initialPlan;
    private List<BigDecimal> changedPlan;
    private List<BigDecimal> actualResult;

    public JsonBurndownLineChart(BurndownLineChart burndownLineChart) {
        this.period = burndownLineChart.period().stream()
                .map(date -> date.format(dtf))
                .collect(Collectors.toList());

        this.initialPlan = burndownLineChart.initialPlan().stream()
                .map(b -> b.setScale(1, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        this.changedPlan = burndownLineChart.changedPlan().stream()
                .map(b -> b.setScale(1, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        this.actualResult = burndownLineChart.actualResult().stream()
                .map(b -> b.setScale(1, RoundingMode.HALF_UP))
                .collect(Collectors.toList());
    }
}
