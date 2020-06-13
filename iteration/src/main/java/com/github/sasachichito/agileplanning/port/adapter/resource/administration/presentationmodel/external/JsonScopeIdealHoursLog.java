package com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.external;

import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Getter
public class JsonScopeIdealHoursLog {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");

    private int planId;
    private String dateTime;
    private BigDecimal scopeIdealHours;
    private String changeType;

    public JsonScopeIdealHoursLog(ScopeIdealHoursLog scopeIdealHoursLog) {
        this.planId = scopeIdealHoursLog.planId().id();
        this.dateTime = dtf.format(scopeIdealHoursLog.dateTime());
        this.scopeIdealHours = scopeIdealHoursLog.scopeIdealHours().hours();
        this.changeType = scopeIdealHoursLog.changeType().name();
    }
}
