package com.github.sasachichito.agileplanning.domain.model.period;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

public class PeriodCalculator {
    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;
    private ResourceRepository resourceRepository;

    public PeriodCalculator(ScopeRepository scopeRepository,
                            StoryRepository storyRepository,
                            ResourceRepository resourceRepository
    ) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
        this.resourceRepository = resourceRepository;
    }

    public Period exec(ScopeId scopeId, ResourceId resourceId) {
        Scope scope = this.scopeRepository.get(scopeId);
        Resource resource = this.resourceRepository.get(resourceId);

        ScopeIdealHours scopeIdealHours = scope.idealHours(new ScopeIdealHoursCalculator(this.storyRepository));
        if (resource.canFinishUp(scopeIdealHours)) {
            return resource.periodForFinishUp(scopeIdealHours);
        }

        throw new IllegalArgumentException("リソースID " + resourceId.id() + " は"
                + "スコープID " + scopeId.id() + " の理想時間を燃焼しきれません。");
    }
}
