package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

public class BurnRelationChecker {
    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;

    @SuppressWarnings("unused")
    public BurnRelationChecker(ScopeRepository scopeRepository, StoryRepository storyRepository) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
    }

    public boolean isRelated(ScopeId scopeId, StoryId storyId) {
        Story story = this.storyRepository.get(storyId);
        return this.scopeRepository.get(scopeId).hasStory(story.storyId());
    }
}
