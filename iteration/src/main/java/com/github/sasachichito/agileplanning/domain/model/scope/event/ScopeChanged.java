package com.github.sasachichito.agileplanning.domain.model.scope.event;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEventSubscriber;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ScopeChanged extends DomainEvent {

    private Scope scope;

    public ScopeChanged(Scope scope) {
        this.scope = scope;
    }

    @Override
    protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber) {
        var subscriber = (Subscriber) domainEventSubscriber;
        subscriber.handle(this);
    }

    public interface Subscriber extends DomainEventSubscriber {
        void handle(ScopeChanged scopeChanged);
    }
}
