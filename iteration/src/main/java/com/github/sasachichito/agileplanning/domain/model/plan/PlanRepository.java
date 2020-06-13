package com.github.sasachichito.agileplanning.domain.model.plan;

import java.util.Set;

public interface PlanRepository {
    Set<Plan> getAll();
    Plan get(PlanId planId);
    void add(Plan plan);
    void put(Plan plan);
    void remove(PlanId planId);
    PlanId nextId();
    void flash();
    boolean exist(PlanId planId);
}
