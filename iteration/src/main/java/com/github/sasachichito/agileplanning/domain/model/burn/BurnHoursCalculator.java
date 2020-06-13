package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.ControlRateForTask;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.math.BigDecimal;

public class BurnHoursCalculator {

    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;

    public BurnHoursCalculator(ScopeRepository scopeRepository, StoryRepository storyRepository) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
    }

    public BigDecimal calculate(Burn burn) {
        Story story = this.storyRepository.getAll().stream()
                .filter(aStory -> aStory.hasTask(burn.taskId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("タスクが存在しません。"));

        Scope scope = this.scopeRepository.getAll().stream()
                .filter(aScope -> aScope.hasStory(story.storyId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("ストーリーが存在しません。"));

        ControlRateForStory controlRateForStory = scope.controlRateForStory(
                new ControlRateForStoryCalculator(this.storyRepository),
                scope.idealHours(new ScopeIdealHoursCalculator(this.storyRepository)));

        ControlRateForTask controlRateForTask = story.controlRateForTask(
                story.idealHours().adjust(controlRateForStory));

        return story.taskIdealHours(burn.taskId()).adjust(controlRateForTask).hours();
    }
}
