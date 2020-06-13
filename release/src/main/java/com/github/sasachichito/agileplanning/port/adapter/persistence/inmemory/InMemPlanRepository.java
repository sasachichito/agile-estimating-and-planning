package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemPlanRepository implements PlanRepository {

    private Map<PlanId, Plan> planMap = new HashMap<>();
    private int lastNumbered = 0;

    @Override
    public Set<Plan> getAll() {
        return this.planMap.values().stream()
                .filter(plan -> !plan.isRemoved())
                .collect(Collectors.toSet());
    }

    @Override
    public Plan get(PlanId planId) {
        if (this.planMap.containsKey(planId) && !this.planMap.get(planId).isRemoved()) {
            return this.planMap.get(planId);
        }
        throw new ResourceNotFoundException("PlanId " + planId.id() + " is not found");
    }

    @Override
    public void add(Plan plan) {
        this.planMap.put(plan.planId(), plan);
    }

    @Override
    public void put(Plan plan) {
        if (plan.planId().id() > this.lastNumbered) {
            this.lastNumbered = plan.planId().id();
        }

        this.planMap.put(plan.planId(), plan);
    }

    @Override
    public void remove(PlanId planId) {
        if (this.planMap.containsKey(planId)) {
            this.planMap.remove(planId);
            return;
        }
        throw new ResourceNotFoundException("PlanId " + planId.id() + " is not found");
    }

    @Synchronized
    @Override
    public PlanId nextId() {
        lastNumbered++;
        return new PlanId(lastNumbered);
    }

    @Override
    public void flash() {
        this.planMap = new HashMap<>();
        this.lastNumbered = 0;
    }

    @Override
    public boolean exist(PlanId planId) {
        return this.planMap.values().stream()
                .filter(plan -> !plan.isRemoved())
                .anyMatch(plan -> plan.planId().equals(planId));
    }
}
