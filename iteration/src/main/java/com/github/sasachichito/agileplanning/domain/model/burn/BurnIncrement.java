package com.github.sasachichito.agileplanning.domain.model.burn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class BurnIncrement {
    private Map<LocalDate, BigDecimal> incrementedHours;

    public BurnIncrement(Map<LocalDate, BigDecimal> incrementedHours) {
        this.incrementedHours = incrementedHours;
    }

    public BigDecimal hoursAt(LocalDate localDate) {
        return this.incrementedHours.getOrDefault(localDate, BigDecimal.ZERO);
    }
}
