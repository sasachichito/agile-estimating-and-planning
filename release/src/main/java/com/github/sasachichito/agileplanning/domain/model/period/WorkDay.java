package com.github.sasachichito.agileplanning.domain.model.period;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(fluent = true)
@Getter
public class WorkDay {
    private LocalDate localDate;

    public WorkDay(LocalDate localDate) {
        this.localDate = localDate;
    }
}
