package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.*;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeChanged;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeRemoved;
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

    public ScopePoint scopePoint(ScopePointCalculator scopePointCalculator) {
        return scopePointCalculator.calculate(this.storyIdList);
    }

    public StoryPointIncrementList storyPointIncrementList(
            StoryPointIncrementGenerator storyPointIncrementGenerator,
            ControlRate controlRate
    ) {
        BigDecimal basePoint = BigDecimal.ZERO;
        List<StoryPointIncrement> storyPointIncrementList = new ArrayList<>();

        for (StoryId storyId : this.storyIdList) {
            StoryPointIncrement storyPointIncrement = storyPointIncrementGenerator.generate(
                    basePoint, storyId, controlRate
            );
            storyPointIncrementList.add(storyPointIncrement);
            basePoint = storyPointIncrement.incrementedStoryPoint();
        }

        return new StoryPointIncrementList(storyPointIncrementList);
    }

    public ControlRate controlRate(ControlRateCalculator controlRateCalculator, ScopePoint scopePoint) {
        return controlRateCalculator.calculate(this.storyIdList, scopePoint);
    }

    public Map<Story, BigDecimal> adjustedStoryPoint(
            ControlRate controlRate,
            AdjustedStoryPointCalculator adjustedStoryPointCalculator
    ) {
        return adjustedStoryPointCalculator.calc(controlRate, this.storyIdList);
    }

    public boolean hasStory(StoryId aStoryId) {
        return this.storyIdList.stream()
                .anyMatch(storyId -> storyId.equals(aStoryId));
    }

    public void removeStory(StoryId aStoryId) {
        var aStoryIdList = this.storyIdList.stream()
                .filter(storyId -> !storyId.equals(aStoryId))
                .collect(Collectors.toList());
        if (aStoryIdList.isEmpty()) {
            this.remove();
            return;
        }
        this.storyIdList = aStoryIdList;
        DomainEventPublisher.instance().publish(new ScopeChanged(this));
    }

    public void change(ScopeTitle scopeTitle, List<StoryId> storyIdList, ScopeSpec scopeSpec) {
        scopeSpec.validate(storyIdList);
        this.scopeTitle = scopeTitle;
        this.storyIdList = storyIdList;
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
        if (Objects.isNull(scopeId)) throw new IllegalArgumentException("ScopeIdは必須です.");
        this.scopeId = scopeId;
    }

    private void setScopeTitle(ScopeTitle scopeTitle) {
        if (Objects.isNull(scopeTitle)) throw new IllegalArgumentException("ScopeTitleは必須です.");
        this.scopeTitle = scopeTitle;
    }

    private void setStoryIdList(List<StoryId> storyIdList) {
        if (Objects.isNull(storyIdList)) throw new IllegalArgumentException("StoryIdListは必須です.");
        if (storyIdList.isEmpty()) throw new IllegalArgumentException("空のStoryIdListは許容されません.");
        if (storyIdList.size() != storyIdList.stream().distinct().count()) throw
                new IllegalArgumentException("StoryIdの重複は許容されません.");

        this.storyIdList = storyIdList;
    }
}
