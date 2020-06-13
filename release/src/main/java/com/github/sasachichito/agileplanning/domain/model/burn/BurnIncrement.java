package com.github.sasachichito.agileplanning.domain.model.burn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class BurnIncrement {
    private Map<LocalDate, BigDecimal> incrementedStoryPoint;

    public BurnIncrement(Map<LocalDate, BigDecimal> incrementedStoryPoint) {
        this.incrementedStoryPoint = incrementedStoryPoint;
    }

    public BigDecimal pointAt(LocalDate localDate) {
        return this.incrementedStoryPoint.get(localDate);
    }
}
