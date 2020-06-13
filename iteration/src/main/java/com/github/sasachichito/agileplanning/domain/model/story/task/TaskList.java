package com.github.sasachichito.agileplanning.domain.model.story.task;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskIncrement;
import com.github.sasachichito.agileplanning.domain.model.story.ControlRateForTask;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.task.event.TaskRemoved;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskList {

    private List<Task> taskList;

    public TaskList(List<Task> taskList) {
        this.setTaskList(taskList);
    }

    public BigDecimal totalEstimate50Pct() {
        return this.taskList.stream()
                .map(Task::estimate50Pct)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalAnxietyVolume() {
        return this.taskList.stream()
                .map(Task::anxietyVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public StoryIncrement increasedStory(BigDecimal baseHours, Story story, ControlRateForTask controlRateForTask) {
        BigDecimal base = baseHours;
        List<TaskIncrement> taskIncrementList = new ArrayList<>();

        for (Task task : this.taskList) {
            TaskIncrement taskIncrement = task.increasedStoryTask(base, story, controlRateForTask);
            base = taskIncrement.idealHoursIncrement().hours();
            taskIncrementList.add(taskIncrement);
        }

        return new StoryIncrement(story, taskIncrementList);
    }

    public void provide(TaskListInterest taskListInterest) {
        taskListInterest.inform(this.taskList);
    }

    public BigDecimal totalTaskIdealHours() {
        return this.taskList.stream()
                .map(Task::idealHours)
                .map(TaskIdealHours::hours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean hasTask(TaskId taskId) {
        return this.taskList.stream()
                .anyMatch(task -> task.taskId().equals(taskId));
    }

    public TaskIdealHours taskIdealHours(TaskId taskId) {
        return this.taskList.stream()
                .filter(task -> task.taskId().equals(taskId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("タスクが存在しません."))
                .idealHours();
    }

    public void replaceTo(TaskList newTaskList) {
        List<Task> removedTaskList =this.taskList.stream()
                .filter(oldTask -> !newTaskList.hasTask(oldTask.taskId()))
                .collect(Collectors.toList());

        removedTaskList.forEach(task -> DomainEventPublisher.instance().publish(new TaskRemoved(task)));
        this.taskList = newTaskList.taskList;
    }

    public void removeAll() {
        this.taskList.forEach(task -> DomainEventPublisher.instance().publish(new TaskRemoved(task)));
    }

    private void setTaskList(List<Task> taskList) {
        if (Objects.isNull(taskList)) throw new IllegalArgumentException("taskListは必須です.");
        this.taskList = taskList;
    }
}
