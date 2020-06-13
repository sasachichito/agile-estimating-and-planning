package com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryChanged;

public class ScopeChanger implements StoryChanged.Subscriber {

    private static final ScopeChanger SCOPE_CHANGER = new ScopeChanger();

    public static ScopeChanger instance() {
        return SCOPE_CHANGER;
    }

    private ScopeChanger() {}

    private ScopeRepository scopeRepository;

    public void setRepositories(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(StoryChanged storyChanged) {
        this.scopeRepository.getAll().stream()
                .filter(scope -> scope.hasStory(storyChanged.story().storyId()))
                .forEach(scope -> scope.change(storyChanged.story()));
    }
}
