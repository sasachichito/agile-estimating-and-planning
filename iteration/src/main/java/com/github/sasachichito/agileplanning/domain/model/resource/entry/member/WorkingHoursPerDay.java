package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

import java.math.BigDecimal;

public class WorkingHoursPerDay {
    private BigDecimal duration;

    public WorkingHoursPerDay(BigDecimal duration) {
        this.duration = duration;
    }

    public BigDecimal hours() {
        return this.duration;
    }
}
