package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

public class BurnSpec {

    private BurnRepository burnRepository;
    private StoryRepository storyRepository;

    public BurnSpec(BurnRepository burnRepository, StoryRepository storyRepository) {
        this.burnRepository = burnRepository;
        this.storyRepository = storyRepository;
    }

    public void validate(Burn burn) {
        boolean taskHadBeenBurned = this.burnRepository.getAll().stream()
                .filter(aBurn -> !aBurn.burnId().equals(burn.burnId()))
                .anyMatch(aBurn -> aBurn.taskId().equals(burn.taskId()));

        if (taskHadBeenBurned) {
            throw new IllegalArgumentException("TaskId " + burn.taskId().id() + " は既にBurnされています。");
        }

        if (!this.storyRepository.exist(burn.taskId())) {
            throw new IllegalArgumentException("TaskId " + burn.taskId().id() + " は存在しません。");
        }
    }
}
