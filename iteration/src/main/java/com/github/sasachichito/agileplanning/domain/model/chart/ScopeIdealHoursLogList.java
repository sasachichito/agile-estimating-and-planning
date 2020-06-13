package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ScopeIdealHoursLogList {
    private List<ScopeIdealHoursLog> scopeIdealHoursLogList;

    public ScopeIdealHoursLogList(List<ScopeIdealHoursLog> scopePointLogList) {
        this.scopeIdealHoursLogList = scopePointLogList;
    }

    public BigDecimal expectedLeftHoursIgnoreChange(LocalDate localDate, WorkingHoursIncrement workingHoursIncrement) {
        BigDecimal initialHours = this.initialHours();
        BigDecimal result = initialHours.subtract(workingHoursIncrement.hoursAt(localDate));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal expectedLeftHours(LocalDate localDate, WorkingHoursIncrement workingHoursIncrement) {
        BigDecimal hoursInTheDay = this.hoursAt(localDate);
        BigDecimal result = hoursInTheDay.subtract(workingHoursIncrement.hoursAt(localDate));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal resultLeftHours(LocalDate localDate, BurnIncrement burnIncrement) {
        BigDecimal hoursInTheDay = this.hoursAt(localDate);
        BigDecimal result = hoursInTheDay.subtract(burnIncrement.hoursAt(localDate));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal initialHours() {
        return this.initialLog().scopeIdealHours().hours();
    }

    public BigDecimal hoursAt(LocalDate localDate) {
        ScopeIdealHoursLog lastLog = this.scopeIdealHoursLogList.stream()
                .filter(scopePointLog -> scopePointLog.dateTime().toLocalDate()
                        .isBefore(localDate))
                .max(Comparator.comparing(ScopeIdealHoursLog::dateTime))
                .orElse(this.initialLog());

        return this.scopeIdealHoursLogList.stream()
                .filter(scopePointLog -> scopePointLog.dateTime().toLocalDate()
                        .equals(localDate))
                .max(Comparator.comparing(ScopeIdealHoursLog::dateTime))
                .map(ScopeIdealHoursLog::scopeIdealHours)
                .map(ScopeIdealHours::hours)
                .orElse(lastLog.scopeIdealHours().hours());
    }

    private ScopeIdealHoursLog initialLog() {
        return this.scopeIdealHoursLogList.stream()
                .filter(ScopeIdealHoursLog::isInitialLog)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("InitialLogがありません."));
    }
}
