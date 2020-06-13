package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.burn.*;
import com.github.sasachichito.agileplanning.domain.model.story.*;
import com.github.sasachichito.agileplanning.domain.model.story.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BurnService {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BurnRepository burnRepository;
    private final StoryRepository storyRepository;

    public Set<Burn> getAll() {
        return this.burnRepository.getAll();
    }

    public Burn createTaskBurn(int taskId) {
        Burn burn = new Burn(
                this.burnRepository.nextId(),
                LocalDate.now(),
                new TaskId(taskId),
                new BurnSpec(this.burnRepository, this.storyRepository));

        this.burnRepository.add(burn);
        return burn;
    }

    public List<Burn> createStoryBurn(int storyId) {
        var taskIdListProvider = new TaskIdListProvider(this.storyRepository.get(new StoryId(storyId)));
        return taskIdListProvider.taskIdList.stream()
                .map(TaskId::id)
                .map(this::createTaskBurn)
                .collect(Collectors.toList());
    }

    public Burn updateOrPut(BurnUpdateCmd burnUpdateCmd) {
        BurnId burnId = new BurnId(burnUpdateCmd.burnId());

        if (this.burnRepository.exist(burnId)) {
            return this.update(
                    this.burnRepository.get(burnId),
                    burnUpdateCmd);
        }
        return this.put(burnUpdateCmd);
    }

    private Burn update(Burn burn, BurnUpdateCmd burnUpdateCmd) {
        burn.change(
                LocalDate.parse(burnUpdateCmd.date(), dtf),
                new TaskId(burnUpdateCmd.taskId()),
                new BurnSpec(this.burnRepository, this.storyRepository)
        );

        return burn;
    }

    private Burn put(BurnUpdateCmd burnUpdateCmd) {
        Burn burn = new Burn(
                new BurnId(burnUpdateCmd.burnId()),
                LocalDate.parse(burnUpdateCmd.date(), dtf),
                new TaskId(burnUpdateCmd.taskId()),
                new BurnSpec(this.burnRepository, this.storyRepository));

        this.burnRepository.put(burn);
        return burn;
    }

    public void deleteBurn(BurnId burnId) {
        Burn burn = this.burnRepository.get(burnId);
        burn.remove();

        this.burnRepository.put(burn);
    }

    public Burn get(BurnId burnId) {
        return this.burnRepository.get(burnId);
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
