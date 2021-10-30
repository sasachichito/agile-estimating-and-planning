package com.github.sasachichito.agileplanning.port.adapter.resource.chart.request;

import java.math.BigDecimal;
import java.util.List;

public class ChartRequest {
    public int planId;
    public List<String> period;
    public int version;
    public BigDecimal scopePoint;
    public List<BigDecimal> changedPlan;
    public String updatedDateTime;
    public String comment;
}