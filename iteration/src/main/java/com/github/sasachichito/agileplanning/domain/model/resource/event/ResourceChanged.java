package com.github.sasachichito.agileplanning.domain.model.resource.event;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEventSubscriber;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ResourceChanged extends DomainEvent {

    private Resource resource;

    public ResourceChanged(Resource resource) {
        this.resource = resource;
    }

    @Override
    protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber) {
        var subscriber = (Subscriber) domainEventSubscriber;
        subscriber.handle(this);
    }

    public interface Subscriber extends DomainEventSubscriber {
        void handle(ResourceChanged resourceChanged);
    }
}
