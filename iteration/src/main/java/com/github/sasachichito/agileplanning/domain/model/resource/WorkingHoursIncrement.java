package com.github.sasachichito.agileplanning.domain.model.resource;

import com.github.sasachichito.agileplanning.domain.model.plan.milestone.IdealHoursIncrement;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class WorkingHoursIncrement {
    private Map<LocalDate, WorkingHours> increasedWorkingHoursMap;

    public WorkingHoursIncrement(Map<LocalDate, WorkingHours> increasedWorkingHoursMap) {
        this.increasedWorkingHoursMap = increasedWorkingHoursMap;
    }

    public WorkingHoursIncrement merge(WorkingHoursIncrement workingHoursIncrement) {
        Map<LocalDate, WorkingHours> merged = new LinkedHashMap<>();
        merged.putAll(this.increasedWorkingHoursMap);
        merged.putAll(workingHoursIncrement.increasedWorkingHoursMap);
        return new WorkingHoursIncrement(merged);
    }

    public LocalDate finishDate(IdealHoursIncrement incrementIdealHours) {
        return this.increasedWorkingHoursMap.entrySet().stream()
                .filter(entry -> entry.getValue().hours.compareTo(incrementIdealHours.hours()) >= 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ストーリーの理想時間がリソースの稼働時間を超えています."))
                .getKey();
    }

    public void provide(WorkingHoursIncrementInterest workingHoursIncrementInterest) {
        workingHoursIncrementInterest.inform(this.increasedWorkingHoursMap);
    }

    public BigDecimal hoursAt(LocalDate localDate) {
        return this.increasedWorkingHoursMap.get(localDate).hours;
    }

    @Accessors(fluent = true)
    public static class WorkingHours {
        @Getter
        BigDecimal hours;

        public WorkingHours(BigDecimal hours) {
            this.hours = hours;
        }
    }
}
