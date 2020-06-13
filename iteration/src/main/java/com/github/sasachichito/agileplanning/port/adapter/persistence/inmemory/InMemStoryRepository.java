package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.story.*;
import com.github.sasachichito.agileplanning.domain.model.story.task.Task;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskListInterest;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemStoryRepository implements StoryRepository {

    private Map<StoryId, Story> storyMap = new HashMap<>();
    private int lastNumberedForStory = 0;
    private int lastNumberedForTask = 0;

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
        if (story.storyId().id() > this.lastNumberedForStory) {
            this.lastNumberedForStory = story.storyId().id();
        }

        var taskIdListProvider = new TaskIdListProvider(story);
        int maxTaskId = taskIdListProvider.taskIdList.stream()
                .mapToInt(TaskId::id)
                .max()
                .orElse(0);

        if (maxTaskId > this.lastNumberedForTask) {
            this.lastNumberedForTask = maxTaskId;
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
        this.lastNumberedForStory++;
        return new StoryId(this.lastNumberedForStory);
    }

    @Synchronized
    @Override
    public TaskId nextTaskId() {
        this.lastNumberedForTask++;
        return new TaskId(this.lastNumberedForTask);
    }

    @Override
    public void flash() {
        this.storyMap = new HashMap<>();
        this.lastNumberedForTask = 0;
        this.lastNumberedForStory = 0;
    }

    @Override
    public boolean exist(StoryId storyId) {
        return this.storyMap.values().stream()
                .filter(story -> !story.isRemoved())
                .anyMatch(story -> story.storyId().equals(storyId));
    }

    @Override
    public boolean exist(TaskId taskId) {
        return this.storyMap.values().stream()
                .filter(story -> !story.isRemoved())
                .anyMatch(story -> story.hasTask(taskId));
    }

    private static class TaskIdListProvider implements StoryInterest, TaskListInterest {

        private List<TaskId> taskIdList;

        private TaskIdListProvider(Story story) {
            story.provide(this);
        }

        @Override
        public void inform(StoryId storyId) {}

        @Override
        public void inform(StoryTitle storyTitle) {}

        @Override
        public void inform(TaskList taskList) {
            taskList.provide(this);
        }

        @Override
        public void inform(List<Task> taskList) {
            this.taskIdList = taskList.stream()
                    .map(Task::taskId)
                    .collect(Collectors.toList());
        }
    }
}
