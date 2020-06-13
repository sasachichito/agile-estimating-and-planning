package com.github.sasachichito.agileplanning.domain.model.plan.event;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEventSubscriber;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class PlanCreated extends DomainEvent {

    @Getter
    private Plan plan;

    public PlanCreated(Plan plan) {
        this.plan = plan;
    }

    @Override
    protected void doYourSubscriber(DomainEventSubscriber domainEventSubscriber) {
        var subscriber = (Subscriber) domainEventSubscriber;
        subscriber.handle(this);
    }

    public interface Subscriber extends DomainEventSubscriber {
        void handle(PlanCreated planCreated);
    }
}
