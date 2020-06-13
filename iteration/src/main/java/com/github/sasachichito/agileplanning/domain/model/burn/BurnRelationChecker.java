package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;

public class BurnRelationChecker {
    private ScopeRepository scopeRepository;
    private StoryRepository storyRepository;

    public BurnRelationChecker(ScopeRepository scopeRepository, StoryRepository storyRepository) {
        this.scopeRepository = scopeRepository;
        this.storyRepository = storyRepository;
    }

    public boolean isRelated(ScopeId scopeId, TaskId taskId) {
        Story story = this.storyRepository.getAll().stream()
                .filter(aStory -> aStory.hasTask(taskId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("タスクが存在しません。"));

        return this.scopeRepository.get(scopeId).hasStory(story.storyId());
    }
}
