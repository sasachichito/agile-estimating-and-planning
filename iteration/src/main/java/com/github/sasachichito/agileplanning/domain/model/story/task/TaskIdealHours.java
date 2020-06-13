package com.github.sasachichito.agileplanning.domain.model.story.task;

import com.github.sasachichito.agileplanning.domain.model.story.ControlRateForTask;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class TaskIdealHours {
    private BigDecimal hours;

    public TaskIdealHours(BigDecimal hours) {
        this.hours = hours;
    }

    public AdjustedTaskIdealHours adjust(ControlRateForTask controlRateForTask) {
        return new AdjustedTaskIdealHours(this.hours.multiply(controlRateForTask.rate()));
    }
}
