package com.github.sasachichito.agileplanning.domain.model.resource.entry.member.velocity;

import com.github.sasachichito.agileplanning.domain.model.story.StoryId;

import java.math.BigDecimal;

public interface ReleasePlanningService {
    BigDecimal storyPoint(StoryId storyId);
}
