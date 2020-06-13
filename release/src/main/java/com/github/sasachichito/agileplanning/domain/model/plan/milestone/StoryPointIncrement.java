package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class StoryPointIncrement {
    private Story story;
    private BigDecimal incrementedStoryPoint;

    public StoryPointIncrement(Story story, BigDecimal incrementedStoryPoint) {
        this.story = story;
        this.incrementedStoryPoint = incrementedStoryPoint;
    }

    public Milestone milestone(VelocityIncrement velocityIncrement) {
        return new Milestone(
                this.story,
                velocityIncrement.finishDate(this.incrementedStoryPoint)
        );
    }
}
