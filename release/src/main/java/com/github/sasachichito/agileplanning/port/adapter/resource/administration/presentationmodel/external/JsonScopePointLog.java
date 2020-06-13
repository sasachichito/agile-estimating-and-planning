package com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.external;

import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Getter
public class JsonScopePointLog {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");

    private int planId;
    private String dateTime;
    private BigDecimal scopePoint;
    private String changeType;

    public JsonScopePointLog(ScopePointLog scopePointLog) {
        this.planId = scopePointLog.planId().id();
        this.dateTime = dtf.format(scopePointLog.dateTime());
        this.scopePoint = scopePointLog.scopePoint().point();
        this.changeType = scopePointLog.changeType().name();
    }
}
