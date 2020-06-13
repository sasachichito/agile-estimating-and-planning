package com.github.sasachichito.agileplanning.domain.model.plan.cost;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;

public class TotalCostCalculator {
    private ResourceRepository resourceRepository;

    public TotalCostCalculator(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public TotalCost calc(Plan plan) {
        Resource resource = this.resourceRepository.get(plan.resourceId());
        return resource.totalCostIn(plan.period());
    }
}
