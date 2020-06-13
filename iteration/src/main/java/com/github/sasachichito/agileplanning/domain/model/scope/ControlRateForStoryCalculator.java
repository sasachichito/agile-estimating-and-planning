package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryIdealHours;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class ControlRateForStoryCalculator {

    private StoryRepository storyRepository;

    public ControlRateForStoryCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public ControlRateForStory calc(List<StoryId> storyIdList, ScopeIdealHours scopeIdealHours) {
        List<Story> storyList = storyIdList.stream()
                .map(this.storyRepository::get)
                .collect(Collectors.toList());

        BigDecimal totalStoryIdealHours = storyList.stream()
                .map(Story::idealHours)
                .map(StoryIdealHours::hours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ControlRateForStory(scopeIdealHours.hours.divide(totalStoryIdealHours, 3, RoundingMode.DOWN));
    }
}
