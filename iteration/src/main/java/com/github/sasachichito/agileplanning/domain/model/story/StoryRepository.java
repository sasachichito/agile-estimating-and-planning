package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;

import java.util.Set;

public interface StoryRepository {
    Set<Story> getAll();
    Story get(StoryId storyId);
    void add(Story story);
    void put(Story story);
    void remove(StoryId storyId);
    StoryId nextStoryId();
    TaskId nextTaskId();
    void flash();
    boolean exist(StoryId storyId);
    boolean exist(TaskId taskId);
}
