package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.AdjustedStoryIdealHours;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdjustedStoryIdealHoursCalculator {

    private StoryRepository storyRepository;

    public AdjustedStoryIdealHoursCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Map<Story, AdjustedStoryIdealHours> calc(ControlRateForStory controlRateForStory, List<StoryId> storyIdList) {
        Map<Story, AdjustedStoryIdealHours> adjustedStoryIdealHoursMap = new LinkedHashMap<>();

        storyIdList.stream()
                .map(storyId -> this.storyRepository.get(storyId))
                .forEach(story ->
                        adjustedStoryIdealHoursMap.put(story, story.idealHours().adjust(controlRateForStory)));

        return adjustedStoryIdealHoursMap;
    }
}
