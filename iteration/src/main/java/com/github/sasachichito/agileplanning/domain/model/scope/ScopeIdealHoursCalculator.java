package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

public class ScopeIdealHoursCalculator {

    private StoryRepository storyRepository;

    public ScopeIdealHoursCalculator(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public ScopeIdealHours calculate(List<StoryId> storyIdList) {
        List<Story> storyList = storyIdList.stream()
                .map(this.storyRepository::get)
                .collect(Collectors.toList());

        BigDecimal totalEstimate50Pct = storyList.stream()
                .map(Story::estimate50Pct)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAnxietyVolume = storyList.stream()
                .map(Story::anxietyVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ScopeIdealHours(
                totalEstimate50Pct.add(totalAnxietyVolume.sqrt(MathContext.DECIMAL64))
        );
    }
}
