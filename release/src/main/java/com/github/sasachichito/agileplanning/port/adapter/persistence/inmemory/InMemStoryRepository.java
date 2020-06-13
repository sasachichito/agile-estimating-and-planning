package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.story.*;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemStoryRepository implements StoryRepository {

    private Map<StoryId, Story> storyMap = new HashMap<>();
    private int lastNumbered = 0;

    @Override
    public Set<Story> getAll() {
        return this.storyMap.values().stream()
                .filter(story -> !story.isRemoved())
                .collect(Collectors.toSet());
    }

    @Override
    public Story get(StoryId storyId) {
        if (this.storyMap.containsKey(storyId) && !this.storyMap.get(storyId).isRemoved()) {
            return this.storyMap.get(storyId);
        }
        throw new ResourceNotFoundException("StoryId " + storyId.id() + " is not found");
    }

    @Override
    public void add(Story story) {
        this.storyMap.put(story.storyId(), story);
    }

    @Override
    public void put(Story story) {
        if (story.storyId().id() > this.lastNumbered) {
            this.lastNumbered = story.storyId().id();
        }

        this.storyMap.put(story.storyId(), story);
    }

    @Override
    public void remove(StoryId storyId) {
        if (this.storyMap.containsKey(storyId)) {
            this.storyMap.remove(storyId);
            return;
        }
        throw new ResourceNotFoundException("StoryId " + storyId.id() + " is not found");
    }

    @Synchronized
    @Override
    public StoryId nextStoryId() {
        this.lastNumbered++;
        return new StoryId(this.lastNumbered);
    }

    @Override
    public void flash() {
        this.storyMap = new HashMap<>();
        this.lastNumbered = 0;
    }

    @Override
    public boolean exist(StoryId storyId) {
        return this.storyMap.values().stream()
                .filter(story -> !story.isRemoved())
                .anyMatch(story -> story.storyId().equals(storyId));
    }
}
