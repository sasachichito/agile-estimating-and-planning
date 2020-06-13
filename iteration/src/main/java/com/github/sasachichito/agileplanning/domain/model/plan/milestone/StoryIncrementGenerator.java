package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.scope.ControlRateForStory;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;

public class StoryIncrementGenerator {
    private StoryRepository storyRepository;

    public StoryIncrementGenerator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public StoryIncrement generate(BigDecimal baseHours, StoryId storyId, ControlRateForStory controlRateForStory) {
        Story story = this.storyRepository.get(storyId);
        return story.increasedStory(baseHours, controlRateForStory);
    }
}
