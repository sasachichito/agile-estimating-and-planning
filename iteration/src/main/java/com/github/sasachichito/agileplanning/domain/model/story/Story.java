package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ControlRateForStory;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryChanged;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryCreated;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskIdealHours;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@Accessors(fluent = true)
@EqualsAndHashCode
public class Story {

    @Getter
    private StoryId storyId;
    @Getter
    private StoryTitle storyTitle;
    private TaskList taskList;
    @Getter
    private boolean isRemoved = false;

    public Story(StoryId storyId, StoryTitle storyTitle, TaskList taskList) {
        this.setStoryId(storyId);
        this.setStoryTitle(storyTitle);
        this.setTaskList(taskList);

        DomainEventPublisher.instance().publish(new StoryCreated(this));
    }

    public BigDecimal estimate50Pct() {
        return this.taskList.totalEstimate50Pct();
    }

    public BigDecimal anxietyVolume() {
        return this.taskList.totalAnxietyVolume();
    }

    public StoryIdealHours idealHours() {
        return new StoryIdealHours(
                this.estimate50Pct().add(this.anxietyVolume().sqrt(MathContext.DECIMAL64))
        );
    }

    public ControlRateForTask controlRateForTask(AdjustedStoryIdealHours adjustedStoryIdealHours) {
        BigDecimal totalTaskIdealHours = this.taskList.totalTaskIdealHours();
        return new ControlRateForTask(adjustedStoryIdealHours.hours().divide(totalTaskIdealHours, 3, RoundingMode.DOWN));
    }

    public void change(StoryTitle storyTitle, TaskList newTaskList) {
        this.setStoryTitle(storyTitle);
        this.taskList.replaceTo(newTaskList);

        DomainEventPublisher.instance().publish(new StoryChanged(this));
    }

    public void remove() {
        this.isRemoved = true;
        DomainEventPublisher.instance().publish(new StoryRemoved(this));
        this.taskList.removeAll();
    }

    public StoryIncrement increasedStory(BigDecimal baseHours, ControlRateForStory controlRateForStory) {
        return this.taskList.increasedStory(
                baseHours,
                this,
                this.controlRateForTask(this.idealHours().adjust(controlRateForStory)));
    }

    public void provide(StoryInterest storyInterest) {
        storyInterest.inform(this.storyId);
        storyInterest.inform(this.storyTitle);
        storyInterest.inform(this.taskList);
    }

    public boolean hasTask(TaskId taskId) {
        return this.taskList.hasTask(taskId);
    }

    public TaskIdealHours taskIdealHours(TaskId taskId) {
        return this.taskList.taskIdealHours(taskId);
    }

    private void setStoryId(StoryId storyId) {
        if (Objects.isNull(storyId)) throw new IllegalArgumentException("StoryIdは必須です.");
        this.storyId = storyId;
    }

    private void setStoryTitle(StoryTitle storyTitle) {
        if (Objects.isNull(storyTitle)) throw new IllegalArgumentException("StoryTitleは必須です.");
        this.storyTitle = storyTitle;
    }

    private void setTaskList(TaskList taskList) {
        if (Objects.isNull(taskList)) throw new IllegalArgumentException("taskListは必須です.");
        this.taskList = taskList;
    }
}
