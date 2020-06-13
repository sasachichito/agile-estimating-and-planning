package com.github.sasachichito.agileplanning.domain.model.period;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Objects;

@Accessors(fluent = true)
public class Period {
    @Getter
    private LocalDate start;
    @Getter
    private LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.setStart(start);
        this.setEnd(end);
        this.validate();
    }

    private void setStart(LocalDate start) {
        if (Objects.isNull(start)) throw new IllegalArgumentException("開始日は必須です.");
        this.start = start;
    }

    private void setEnd(LocalDate end) {
        if (Objects.isNull(end)) throw new IllegalArgumentException("終了日は必須です.");
        this.end = end;
    }

    private void validate() {
        if (this.start.isAfter(this.end)) {
            throw new IllegalArgumentException("開始日は終了日の前である必要があります.");
        }
    }
}
