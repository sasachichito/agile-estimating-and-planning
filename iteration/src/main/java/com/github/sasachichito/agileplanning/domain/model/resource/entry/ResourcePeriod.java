package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.period.HolidayList;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDay;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDayListGenerator;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Accessors(fluent = true)
public class ResourcePeriod {
    @Getter
    private LocalDate start;
    @Getter
    private LocalDate end;

    public ResourcePeriod(LocalDate start, LocalDate end) {
        this.setStart(start);
        this.setEnd(end);
        this.validate();
    }

    int numOfWorkingDays() {
        return this.workDayList().size();
    }

    int numOfWorkingDaysIn(Period period) {
        return this.workDayListFor(period).size();
    }

    LocalDate nthWorkDay(int nth) {
        return this.workDayList().get(nth - 1).localDate();
    }

    private List<WorkDay> workDayList() {
        return WorkDayListGenerator.exec(this.start, this.end);
    }

    List<WorkDay> workDayListFor(Period period) {
        LocalDate start = period.start().isAfter(this.start)
                ? period.start()
                : this.start;

        LocalDate end = period.end().isBefore(this.end)
                ? period.end()
                : this.end;

        return WorkDayListGenerator.exec(start, end);
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
