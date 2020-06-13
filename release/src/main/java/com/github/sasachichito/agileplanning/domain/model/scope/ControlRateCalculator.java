package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class ControlRateCalculator {
    private StoryRepository storyRepository;

    public ControlRateCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public ControlRate calculate(List<StoryId> storyIdList, ScopePoint scopePoint) {
        List<Story> storyList = storyIdList.stream()
                .map(this.storyRepository::get)
                .collect(Collectors.toList());

        BigDecimal totalStoryPoint = storyList.stream()
                .map(Story::simpleStoryPoint)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ControlRate(
                scopePoint.point().divide(totalStoryPoint, 3, RoundingMode.DOWN)
        );
    }
}
