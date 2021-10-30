package com.github.sasachichito.agileplanning.domain.model.release;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;

import java.math.BigDecimal;

public interface ReleasePlanningService {
    BigDecimal storyPoint(StoryId storyId);
    void createStory(Story story);
    void updateStory(Story story);
    void deleteStory(Story story);
}
