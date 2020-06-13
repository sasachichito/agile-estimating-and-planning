package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

public class ScopePointCalculator {

    private StoryRepository storyRepository;

    public ScopePointCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public ScopePoint calculate(List<StoryId> storyIdList) {
        List<Story> storyList = storyIdList.stream()
                .map(this.storyRepository::get)
                .collect(Collectors.toList());

        int totalEstimate50pct = storyList.stream()
                .map(Story::storyPointEstimated50pct)
                .mapToInt(Integer::intValue)
                .sum();

        BigDecimal totalAnxietyVolume = storyList.stream()
                .map(Story::getAnxietyVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ScopePoint(
                BigDecimal.valueOf(totalEstimate50pct).add(
                        totalAnxietyVolume.sqrt(MathContext.DECIMAL64)
                )
        );
    }
}
