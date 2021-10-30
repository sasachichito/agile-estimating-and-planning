package com.github.sasachichito.agileplanning.domain.model.event;

import com.github.sasachichito.agileplanning.domain.model.burn.event.subscriber.BurnTaskRemover;
import com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber.ScopeIdealHoursLogger;
import com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber.PlanAdjuster;
import com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber.PlanRemover;
import com.github.sasachichito.agileplanning.domain.model.release.StoryLinker;
import com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber.ScopeChanger;
import com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber.ScopeStoryRemover;

import java.util.ArrayList;
import java.util.List;

public class DomainEventPublisher {

    private static final DomainEventPublisher instance = new DomainEventPublisher();

    public static DomainEventPublisher instance() {
        return instance;
    }

    private List<DomainEventSubscriber> subscriberList = new ArrayList<>();

    private DomainEventPublisher() {
        this.addSubscriber(StoryLinker.instance());
        this.addSubscriber(PlanAdjuster.instance());
        this.addSubscriber(PlanRemover.instance());
        this.addSubscriber(ScopeStoryRemover.instance());
        this.addSubscriber(ScopeChanger.instance());
        this.addSubscriber(BurnTaskRemover.instance());
        this.addSubscriber(ScopeIdealHoursLogger.instance());
    }

    public void publish(DomainEvent domainEvent) {
        if (domainEvent == null) {
            return;
        }

        if (!this.hasSubscribers()) {
            return;
        }

        this.subscriberList.forEach(subscriber -> subscriber.handleEvent(domainEvent));
    }

    public boolean hasSubscribers() {
        return !(subscriberList == null || subscriberList.isEmpty());
    }

    public DomainEventPublisher addSubscriber(DomainEventSubscriber domainEventSubscriber) {
        this.subscriberList.add(domainEventSubscriber);
        return this;
    }
}
