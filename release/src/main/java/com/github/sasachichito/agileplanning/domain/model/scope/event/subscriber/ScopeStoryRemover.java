package com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;

public class ScopeStoryRemover implements StoryRemoved.Subscriber {

    private static final ScopeStoryRemover SCOPE_STORY_REMOVER = new ScopeStoryRemover();

    public static ScopeStoryRemover instance() {
        return SCOPE_STORY_REMOVER;
    }

    private ScopeStoryRemover() {}

    private ScopeRepository scopeRepository;

    public void setRepositories(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(StoryRemoved storyRemoved) {
        scopeRepository.getAll().stream()
                .filter(scope -> scope.hasStory(storyRemoved.story().storyId()))
                .forEach(scope -> {
                    scope.removeStory(storyRemoved.story().storyId());
                    this.scopeRepository.put(scope);
                });
    }
}
