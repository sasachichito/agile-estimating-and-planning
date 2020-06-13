package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdjustedStoryPointCalculator {
    private StoryRepository storyRepository;

    public AdjustedStoryPointCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Map<Story, BigDecimal> calc(ControlRate controlRate, List<StoryId> storyIdList) {
        Map<Story, BigDecimal> adjustedStoryPointMap = new LinkedHashMap<>();

        storyIdList.stream()
                .map(storyId -> this.storyRepository.get(storyId))
                .forEach(story ->
                        adjustedStoryPointMap.put(story, story.simpleStoryPoint().multiply(controlRate.rate())));

        return adjustedStoryPointMap;
    }
}
