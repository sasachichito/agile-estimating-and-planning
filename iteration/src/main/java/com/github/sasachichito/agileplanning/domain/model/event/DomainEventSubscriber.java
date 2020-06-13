package com.github.sasachichito.agileplanning.domain.model.event;

public interface DomainEventSubscriber {
    void handleEvent(final DomainEvent domainEvent);
}
