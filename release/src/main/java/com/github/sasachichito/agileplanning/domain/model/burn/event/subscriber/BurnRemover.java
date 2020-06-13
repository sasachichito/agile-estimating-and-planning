package com.github.sasachichito.agileplanning.domain.model.burn.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;

public class BurnRemover implements StoryRemoved.Subscriber {
    private static final BurnRemover BURN_REMOVER = new BurnRemover();

    public static BurnRemover instance() {
        return BURN_REMOVER;
    }

    private BurnRemover() {}

    private BurnRepository burnRepository;

    public void setRepositories(BurnRepository burnRepository) {
        this.burnRepository = burnRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(StoryRemoved storyRemoved) {
        this.burnRepository.getAll().stream()
                .filter(burn -> burn.storyId().equals(storyRemoved.story().storyId()))
                .forEach(burn -> {
                    burn.remove();
                    this.burnRepository.put(burn);
                });
    }
}
