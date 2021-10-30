package com.github.sasachichito.agileplanning.domain.model.story.event;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEventSubscriber;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class StoryCreated extends DomainEvent {

    private Story story;

    public StoryCreated(Story story) {
        this.story = story;
    }

    @Override
    protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber) {
        var subscriber = (Subscriber) domainEventSubscriber;
        subscriber.handle(this);
    }

    public interface Subscriber extends DomainEventSubscriber {
        void handle(StoryCreated storyCreated);
    }
}
