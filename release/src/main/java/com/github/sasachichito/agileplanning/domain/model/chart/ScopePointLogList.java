package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ScopePointLogList {
    private List<ScopePointLog> scopePointLogList;

    public ScopePointLogList(List<ScopePointLog> scopePointLogList) {
        this.scopePointLogList = scopePointLogList;
    }

    public BigDecimal expectedLeftPointIgnoreChange(LocalDate localDate, VelocityIncrement velocityIncrement) {
        BigDecimal initialPoint = this.initialPoint();
        BigDecimal result = initialPoint.subtract(BigDecimal.valueOf(velocityIncrement.pointAt(localDate)));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal expectedLeftPoint(LocalDate localDate, VelocityIncrement velocityIncrement) {
        BigDecimal pointInTheDay = this.pointAt(localDate);
        BigDecimal result = pointInTheDay.subtract(BigDecimal.valueOf(velocityIncrement.pointAt(localDate)));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal resultLeftPoint(LocalDate localDate, BurnIncrement burnIncrement) {
        BigDecimal pointInTheDay = this.pointAt(localDate);
        BigDecimal result = pointInTheDay.subtract(burnIncrement.pointAt(localDate));
        return (result.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : result;
    }

    public BigDecimal initialPoint() {
        return this.initialLog().scopePoint().point();
    }

    public BigDecimal pointAt(LocalDate localDate) {
        ScopePointLog lastLog = this.scopePointLogList.stream()
                .filter(scopePointLog -> scopePointLog.dateTime().toLocalDate()
                        .isBefore(localDate))
                .max(Comparator.comparing(ScopePointLog::dateTime))
                .orElse(this.initialLog());

        return this.scopePointLogList.stream()
                .filter(scopePointLog -> scopePointLog.dateTime().toLocalDate()
                        .equals(localDate))
                .max(Comparator.comparing(ScopePointLog::dateTime))
                .map(ScopePointLog::scopePoint)
                .map(ScopePoint::point)
                .orElse(lastLog.scopePoint().point());
    }

    private ScopePointLog initialLog() {
        return this.scopePointLogList.stream()
                .filter(ScopePointLog::isInitialLog)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("InitialLogがありません."));
    }
}
