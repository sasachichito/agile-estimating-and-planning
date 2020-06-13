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
        boolean allReadyBurn = this.burnRepository.getAll().stream()
                .filter(aBurn -> !aBurn.burnId().equals(burn.burnId()))
                .anyMatch(aBurn -> aBurn.storyId().equals(burn.storyId()));

        if (allReadyBurn) {
            throw new IllegalArgumentException("StoryId " + burn.storyId().id() + " は既にBurnされています。");
        }

        if (!this.storyRepository.exist(burn.storyId())) {
            throw new IllegalArgumentException("StoryId " + burn.storyId().id() + " は存在しません。");
        }
    }
}
