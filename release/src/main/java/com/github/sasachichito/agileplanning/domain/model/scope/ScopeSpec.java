package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;

import java.util.List;

public class ScopeSpec {
    private StoryRepository storyRepository;

    public ScopeSpec(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public void validate(List<StoryId> storyIdList) {
        storyIdList.stream()
                .filter(storyId -> !this.storyRepository.exist(storyId))
                .findFirst()
                .ifPresent(storyId -> {
                    throw new IllegalArgumentException("ストーリーID " + storyId.id() + " は存在しません.");
                });
    }
}
