package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.story.StoryCreateCmd;
import com.github.sasachichito.agileplanning.application.command.story.StoryUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.story.*;
import com.github.sasachichito.agileplanning.domain.model.story.task.Task;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoryService {

    private final StoryRepository storyRepository;

    public Story create(StoryCreateCmd storyCreateCmd) {
        List<Task> taskList = storyCreateCmd.taskList().stream()
                .map(taskCreate -> new Task(
                        this.storyRepository.nextTaskId(),
                        new TaskName(taskCreate.taskName()),
                        taskCreate.estimate50Pct(), taskCreate.estimate90Pct())
                )
                .collect(Collectors.toList());

        Story story = new Story(
                this.storyRepository.nextStoryId(),
                new StoryTitle(storyCreateCmd.storyTitle()),
                new TaskList(taskList)
        );

        storyRepository.add(story);
        return story;
    }

    public Story get(int storyId) {
        return this.storyRepository.get(new StoryId(storyId));
    }

    public Set<Story> getAll() {
        return this.storyRepository.getAll();
    }

    public void delete(int storyId) {
        Story story = this.storyRepository.get(new StoryId(storyId));
        story.remove();
        this.storyRepository.put(story);
    }

    public Story updateOrPut(StoryUpdateCmd storyUpdateCmd) {
        StoryId storyId = new StoryId(storyUpdateCmd.storyId());

        if (this.storyRepository.exist(storyId)) {
            return this.update(
                    this.storyRepository.get(storyId),
                    storyUpdateCmd);
        }
        return this.put(storyUpdateCmd);
    }

    private Story put(StoryUpdateCmd storyUpdateCmd) {
        List<Task> taskList = storyUpdateCmd.taskList().stream()
                .map(taskCreate -> {
                    TaskId taskId = new TaskId(taskCreate.taskId());

                    if (this.storyRepository.exist(taskId)) {
                        throw new IllegalArgumentException("taskId " + taskId.id() + " は既に別のストーリーで登録されています。");
                    }

                    return new Task(
                            taskId,
                            new TaskName(taskCreate.taskName()),
                            taskCreate.estimate50Pct(), taskCreate.estimate90Pct());
                })
                .collect(Collectors.toList());

        Story story = new Story(
                new StoryId(storyUpdateCmd.storyId()),
                new StoryTitle(storyUpdateCmd.storyTitle()),
                new TaskList(taskList)
        );

        storyRepository.put(story);
        return story;
    }

    private Story update(Story story, StoryUpdateCmd storyUpdateCmd) {
        List<Task> taskList = storyUpdateCmd.taskList().stream()
                .map(taskUpdate -> {
                    TaskId taskId = story.hasTask(new TaskId(taskUpdate.taskId()))
                            ? new TaskId(taskUpdate.taskId())
                            : this.storyRepository.nextTaskId();
                    return new Task(
                            taskId,
                            new TaskName(taskUpdate.taskName()),
                            taskUpdate.estimate50Pct(),
                            taskUpdate.estimate90Pct());
                })
                .collect(Collectors.toList());

        story.change(new StoryTitle(storyUpdateCmd.storyTitle()), new TaskList(taskList));

        this.storyRepository.put(story);
        return story;
    }
}
