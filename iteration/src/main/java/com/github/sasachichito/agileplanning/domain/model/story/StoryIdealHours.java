package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.scope.ControlRateForStory;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class StoryIdealHours {
    @Getter
    private BigDecimal hours;

    public StoryIdealHours(BigDecimal hours) {
        this.hours = hours;
    }

    public AdjustedStoryIdealHours adjust(ControlRateForStory controlRateForStory) {
        return new AdjustedStoryIdealHours(this.hours.multiply(controlRateForStory.rate()));
    }
}
