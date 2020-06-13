package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import com.github.sasachichito.agileplanning.domain.model.scope.ControlRate;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;

public class StoryPointIncrementGenerator {
    private StoryRepository storyRepository;

    public StoryPointIncrementGenerator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public StoryPointIncrement generate(BigDecimal basePoint, StoryId storyId, ControlRate controlRate) {
        Story story = this.storyRepository.get(storyId);
        return story.storyPointIncrement(basePoint, controlRate);
    }
}
