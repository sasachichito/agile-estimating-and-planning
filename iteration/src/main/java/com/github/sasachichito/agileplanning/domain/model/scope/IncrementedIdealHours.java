package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryIdealHours;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(fluent = true)
public class IncrementedIdealHours {
    @Getter
    private Map<Story, StoryIdealHours> incrementedIdealHours;

    public IncrementedIdealHours(Map<Story, StoryIdealHours> incrementedIdealHours) {
        this.incrementedIdealHours = incrementedIdealHours;
    }
}
