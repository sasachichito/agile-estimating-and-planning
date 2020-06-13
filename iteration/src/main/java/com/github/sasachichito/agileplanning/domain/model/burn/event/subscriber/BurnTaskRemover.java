package com.github.sasachichito.agileplanning.domain.model.burn.event.subscriber;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.story.task.event.TaskRemoved;

public class BurnTaskRemover implements TaskRemoved.Subscriber {
    private static final BurnTaskRemover BURN_TASK_REMOVER = new BurnTaskRemover();

    public static BurnTaskRemover instance() {
        return BURN_TASK_REMOVER;
    }

    private BurnTaskRemover() {}

    private BurnRepository burnRepository;

    public void setRepositories(BurnRepository burnRepository) {
        this.burnRepository = burnRepository;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(TaskRemoved taskRemoved) {
        this.burnRepository.getAll().stream()
                .filter(burn -> burn.taskId().equals(taskRemoved.task().taskId()))
                .forEach(burn -> {
                    burn.remove();
                    this.burnRepository.put(burn);
                });
    }
}
