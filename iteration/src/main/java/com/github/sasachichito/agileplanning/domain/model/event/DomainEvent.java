package com.github.sasachichito.agileplanning.domain.model.event;

public abstract class DomainEvent {

    public void subscribed(DomainEventSubscriber domainEventSubscriber) {
        try {
            this.doYourSubscriber(domainEventSubscriber);
        } catch (ClassCastException ignored) {}
    }

    abstract protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber);
}
