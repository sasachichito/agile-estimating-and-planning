package com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceRemoved;
import com.github.sasachichito.agileplanning.domain.model.scope.event.ScopeRemoved;

public class PlanRemover implements ScopeRemoved.Subscriber, ResourceRemoved.Subscriber {

    private static final PlanRemover PLAN_REMOVER = new PlanRemover();

    public static PlanRemover instance() {
        return PLAN_REMOVER;
    }

    private PlanRemover() {}

    private PlanRepository planRepository;

    public void setRepositories(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(ResourceRemoved resourceRemoved) {
        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasResource(resourceRemoved.resource().resourceId()))
                .forEach(plan -> {
                    plan.remove();
                    this.planRepository.put(plan);
                });
    }

    @Override
    public void handle(ScopeRemoved scopeRemoved) {
        this.planRepository.getAll().stream()
                .filter(plan -> plan.hasScope(scopeRemoved.scope().scopeId()))
                .forEach(plan -> this.planRepository.remove(plan.planId()));
    }
}
