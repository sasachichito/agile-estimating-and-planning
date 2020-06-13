package com.github.sasachichito.agileplanning.domain.model.story;

import java.util.Set;

public interface StoryRepository {
    Set<Story> getAll();
    Story get(StoryId storyId);
    void add(Story story);
    void put(Story story);
    void remove(StoryId storyId);
    StoryId nextStoryId();
    void flash();
    boolean exist(StoryId storyId);
}
