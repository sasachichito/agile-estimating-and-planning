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

        ScopePoint scopePoint = scope.scopePoint(new ScopePointCalculator(this.storyRepository));
        if (resource.canFinishUp(scopePoint)) {
            return resource.periodForFinishUp(scopePoint);
        }

        throw new IllegalArgumentException("リソースID " + resourceId.id() + " は"
                + "スコープID " + scopeId.id() + " の合計ストーリーポイントを燃焼しきれません。");
    }
}
