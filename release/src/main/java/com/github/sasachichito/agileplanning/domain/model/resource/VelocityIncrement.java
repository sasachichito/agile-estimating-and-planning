package com.github.sasachichito.agileplanning.domain.model.resource;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Accessors(fluent = true)
@Getter
public class VelocityIncrement {
    private Map<LocalDate, Integer> incrementedStoryPoint;

    public VelocityIncrement(Map<LocalDate, Integer> incrementedStoryPoint) {
        this.incrementedStoryPoint = incrementedStoryPoint;
    }

    public VelocityIncrement merge(VelocityIncrement velocityIncrement) {
        Map<LocalDate, Integer> merged = new LinkedHashMap<>();
        merged.putAll(this.incrementedStoryPoint);
        merged.putAll(velocityIncrement.incrementedStoryPoint);
        return new VelocityIncrement(merged);
    }

    public LocalDate finishDate(BigDecimal incrementedStoryPoint) {
        return this.incrementedStoryPoint.entrySet().stream()
                .filter(entry -> BigDecimal.valueOf(entry.getValue()).compareTo(incrementedStoryPoint) >= 0)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getKey();
    }

    public Integer pointAt(LocalDate localDate) {
        return this.incrementedStoryPoint.get(localDate);
    }
}
