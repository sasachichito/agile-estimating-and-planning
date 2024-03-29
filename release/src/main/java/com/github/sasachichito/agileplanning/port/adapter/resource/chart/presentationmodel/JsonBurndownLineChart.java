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

    public static final DateTimeFormatter periodDtf = DateTimeFormatter.ofPattern("yyyy/M/d");
    public static final DateTimeFormatter updatedDateTimeDtf = DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss");

    private int planId;
    private List<String> period;
    private Integer version;
    private BigDecimal scopePoint;
    private List<BigDecimal> changedPlan;
    private List<BigDecimal> actualResult;
    private String updatedDateTime;
    private String comment;

    public JsonBurndownLineChart(BurndownLineChart burndownLineChart) {
        this.planId = burndownLineChart.planId().id();

        this.period = burndownLineChart.period().stream()
                .map(date -> date.format(periodDtf))
                .collect(Collectors.toList());

        this.version = burndownLineChart.version();

        this.scopePoint = burndownLineChart.scopePoint().point();

        this.changedPlan = burndownLineChart.changedPlan().stream()
                .map(b -> b.setScale(1, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        this.actualResult = burndownLineChart.actualResult().stream()
                .map(b -> b.setScale(1, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        this.updatedDateTime = burndownLineChart.updatedDateTime()
                .format(updatedDateTimeDtf);

        this.comment = burndownLineChart.comment() == null ? "" : burndownLineChart.comment();
    }
}
