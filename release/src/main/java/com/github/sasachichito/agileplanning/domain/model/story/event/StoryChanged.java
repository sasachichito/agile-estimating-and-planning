package com.github.sasachichito.agileplanning.domain.model.story.event;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEventSubscriber;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class StoryChanged extends DomainEvent {

    private Story story;

    public StoryChanged(Story story) {
        this.story = story;
    }

    @Override
    protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber) {
        var subscriber = (Subscriber) domainEventSubscriber;
        subscriber.handle(this);
    }

    public interface Subscriber extends DomainEventSubscriber {
        void handle(StoryChanged storyChanged);
    }
}
