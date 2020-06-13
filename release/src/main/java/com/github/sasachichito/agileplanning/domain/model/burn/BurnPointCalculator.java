package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;

public class BurnPointCalculator {

    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;

    public BurnPointCalculator(ScopeRepository scopeRepository, StoryRepository storyRepository) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
    }

    public BigDecimal calculate(Burn burn) {
        Story story = this.storyRepository.get(burn.storyId());

        Scope scope = this.scopeRepository.getAll().stream()
                .filter(aScope -> aScope.hasStory(story.storyId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("StoryId: " + burn.storyId() + " を含むスコープが存在しません。"));

        ControlRate controlRate = scope.controlRate(
                new ControlRateCalculator(this.storyRepository),
                scope.scopePoint(new ScopePointCalculator(this.storyRepository)));

        return story.simpleStoryPoint().multiply(controlRate.rate());
    }
}
