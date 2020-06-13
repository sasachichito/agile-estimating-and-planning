package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(fluent = true)
@Getter
public class Milestone {
    private Story story;
    private LocalDate localDate;

    public Milestone(Story story, LocalDate localDate) {
        this.story = story;
        this.localDate = localDate;
    }
}
