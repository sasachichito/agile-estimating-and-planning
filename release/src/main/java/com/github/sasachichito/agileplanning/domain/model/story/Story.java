package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryPointIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ControlRate;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryChanged;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryCreated;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Accessors(fluent = true)
@EqualsAndHashCode
public class Story {

    @Getter
    private StoryId storyId;
    @Getter
    private StoryTitle storyTitle;
    private StoryPoint storyPoint;
    private List<String> links;
    @Getter
    private boolean isRemoved = false;

    public Story(StoryId storyId, StoryTitle storyTitle, StoryPoint storyPoint, List<String> links) {
        this.setStoryId(storyId);
        this.setStoryTitle(storyTitle);
        this.setStoryPoint(storyPoint);
        this.setLinks(links);

        DomainEventPublisher.instance().publish(new StoryCreated(this));
    }

    public int storyPointEstimated50pct() {
        return this.storyPoint.estimate50pct();
    }

    public BigDecimal getAnxietyVolume() {
        return this.storyPoint.anxietyVolume();
    }

    public BigDecimal simpleStoryPoint() {
        return this.storyPoint.simple();
    }

    public void change(StoryTitle storyTitle, StoryPoint storyPoint, List<String> links) {
        this.setStoryTitle(storyTitle);
        this.setStoryPoint(storyPoint);
        this.setLinks(links);

        DomainEventPublisher.instance().publish(new StoryChanged(this));
    }

    public void remove() {
        this.isRemoved = true;
        DomainEventPublisher.instance().publish(new StoryRemoved(this));
    }

    public StoryPointIncrement storyPointIncrement(BigDecimal basePoint, ControlRate controlRate) {
        return new StoryPointIncrement(
                this,
                basePoint.add(this.storyPoint.simple().multiply(controlRate.rate())));
    }

    public void provide(StoryInterest storyInterest) {
        storyInterest.inform(this.storyId);
        storyInterest.inform(this.storyTitle);
        storyInterest.inform(this.storyPoint);
        storyInterest.informLinks(this.links);
    }

    private void setStoryId(StoryId storyId) {
        if (Objects.isNull(storyId)) throw new IllegalArgumentException("StoryIdは必須です.");
        this.storyId = storyId;
    }

    private void setStoryTitle(StoryTitle storyTitle) {
        if (Objects.isNull(storyTitle)) throw new IllegalArgumentException("StoryTitleは必須です.");
        this.storyTitle = storyTitle;
    }

    private void setStoryPoint(StoryPoint storyPoint) {
        if (Objects.isNull(storyPoint)) throw new IllegalArgumentException("StoryPointは必須です.");
        this.storyPoint = storyPoint;
    }

    private void setLinks(List<String> links) {
        if (Objects.isNull(links)) throw new IllegalArgumentException("linksにnullは設定できません.");
        this.links = links;
    }
}
