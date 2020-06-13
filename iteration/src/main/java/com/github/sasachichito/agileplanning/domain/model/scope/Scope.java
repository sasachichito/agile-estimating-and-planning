package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryIncrementList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryIncrementGenerator;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeRemoved;
import com.github.sasachichito.agileplanning.domain.model.story.AdjustedStoryIdealHours;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Accessors(fluent = true)
public class Scope {

    @Getter
    private ScopeId scopeId;
    private ScopeTitle scopeTitle;
    private List<StoryId> storyIdList;
    @Getter
    private boolean isRemoved = false;

    public Scope(ScopeId scopeId, ScopeTitle scopeTitle, List<StoryId> storyIdList, ScopeSpec scopeSpec) {
        this.setScopeId(scopeId);
        this.setScopeTitle(scopeTitle);
        this.setStoryIdList(storyIdList);
        scopeSpec.validate(this.storyIdList);
    }

    public ScopeIdealHours idealHours(ScopeIdealHoursCalculator scopeIdealHoursCalculator) {
        return scopeIdealHoursCalculator.calculate(this.storyIdList);
    }

    public StoryIncrementList increasedStoryList(StoryIncrementGenerator storyIncrementGenerator, ControlRateForStory controlRateForStory) {
        BigDecimal baseHours = BigDecimal.ZERO;
        List<StoryIncrement> storyIncrementList = new ArrayList<>();

        for (StoryId storyId : this.storyIdList) {
            StoryIncrement storyIncrement = storyIncrementGenerator.generate(baseHours, storyId, controlRateForStory);
            storyIncrementList.add(storyIncrement);
            baseHours = storyIncrement.lastHours();
        }

        return new StoryIncrementList(storyIncrementList);
    }

    public ControlRateForStory controlRateForStory(ControlRateForStoryCalculator calculator, ScopeIdealHours scopeIdealHours) {
        return calculator.calc(this.storyIdList, scopeIdealHours);
    }

    public Map<Story, AdjustedStoryIdealHours> adjustedStoryIdealHours(
            ControlRateForStory controlRateForStory,
            AdjustedStoryIdealHoursCalculator adjustedStoryIdealHoursCalculator
    ) {
        return adjustedStoryIdealHoursCalculator.calc(controlRateForStory, this.storyIdList);
    }

    public boolean hasStory(StoryId aStoryId) {
        return this.storyIdList.stream()
                .anyMatch(storyId -> storyId.equals(aStoryId));
    }

    public void removeStory(StoryId aStoryId) {
        // TODO 仕様クラスによるバリデーション
        this.storyIdList = this.storyIdList.stream()
                .filter(storyId -> !storyId.equals(aStoryId))
                .collect(Collectors.toList());
        DomainEventPublisher.instance().publish(new ScopeChanged(this));
    }

    public void change(ScopeTitle scopeTitle, List<StoryId> storyIdList, ScopeSpec scopeSpec) {
        scopeSpec.validate(storyIdList);
        this.setScopeTitle(scopeTitle);
        this.setStoryIdList(storyIdList);
        DomainEventPublisher.instance().publish(new ScopeChanged(this));
    }

    public void change(Story story) {
        DomainEventPublisher.instance().publish(new ScopeChanged(this));
    }

    public void remove() {
        this.isRemoved = true;
        DomainEventPublisher.instance().publish(new ScopeRemoved(this));
    }

    public void provide(ScopeInterest scopeInterest) {
        scopeInterest.inform(this.scopeId);
        scopeInterest.inform(this.scopeTitle);
        scopeInterest.inform(this.storyIdList);
    }

    private void setScopeId(ScopeId scopeId) {
        if (Objects.isNull(scopeId)) throw new IllegalArgumentException("スコープIDは必須です.");
        this.scopeId = scopeId;
    }

    private void setScopeTitle(ScopeTitle scopeTitle) {
        if (Objects.isNull(scopeTitle)) throw new IllegalArgumentException("スコープタイトルは必須です.");
        this.scopeTitle = scopeTitle;
    }

    private void setStoryIdList(List<StoryId> storyIdList) {
        if (Objects.isNull(storyIdList)) throw new IllegalArgumentException("ストーリーIDリストは必須です.");
        if (storyIdList.isEmpty()) throw new IllegalArgumentException("空のストーリーIDリストは許容されません.");
        if (storyIdList.size() != storyIdList.stream().distinct().count()) throw
                new IllegalArgumentException("ストーリーIDの重複は許容されません.");

        this.storyIdList = storyIdList;
    }
}
