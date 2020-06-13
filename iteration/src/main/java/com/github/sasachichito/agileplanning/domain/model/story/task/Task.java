package com.github.sasachichito.agileplanning.domain.model.story.task;

import com.github.sasachichito.agileplanning.domain.model.plan.milestone.IdealHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskAndRelatedStory;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskIncrement;
import com.github.sasachichito.agileplanning.domain.model.story.ControlRateForTask;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Accessors(fluent = true)
public class Task {
    @Getter
    private TaskId taskId;
    @Getter
    private TaskName taskName;
    @Getter
    private BigDecimal estimate50Pct;
    @Getter
    private BigDecimal estimate90Pct;

    public Task(TaskId taskId, TaskName taskName, BigDecimal estimate50Pct, BigDecimal estimate90Pct) {
        this.setTaskId(taskId);
        this.setTaskName(taskName);
        this.setEstimate50Pct(estimate50Pct);
        this.setEstimate90Pct(estimate90Pct);
        this.validate();
    }

    BigDecimal anxietyVolume() {
        return this.estimate90Pct.subtract(this.estimate50Pct).pow(2);
    }

    public TaskIdealHours idealHours() {
        BigDecimal subDiv2 = this.estimate90Pct.subtract(this.estimate50Pct).divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_UP);
        return new TaskIdealHours(this.estimate50Pct.add(subDiv2));
    }

    public TaskIncrement increasedStoryTask(BigDecimal baseHours, Story story, ControlRateForTask controlRateForTask) {
        return new TaskIncrement(
                new TaskAndRelatedStory(this, story),
                new IdealHoursIncrement(baseHours.add(this.idealHours().adjust(controlRateForTask).hours()))
        );
    }

    public void provide(TaskInterest taskInterest) {
        taskInterest.inform(this.taskId);
        taskInterest.inform(this.taskName);
        taskInterest.inform50(this.estimate50Pct);
        taskInterest.inform90(this.estimate90Pct);
    }

    private void setTaskId(TaskId taskId) {
        if (Objects.isNull(taskId)) throw new IllegalArgumentException("TaskIdは必須です.");
        this.taskId = taskId;
    }

    private void setTaskName(TaskName taskName) {
        if (Objects.isNull(taskName)) throw new IllegalArgumentException("TaskNameは必須です.");
        this.taskName = taskName;
    }

    private void setEstimate50Pct(BigDecimal estimate50Pct) {
        if (estimate50Pct == null) throw new IllegalArgumentException("50%見積もりは必須です.");
        if (estimate50Pct.compareTo(BigDecimal.ZERO) == 0) throw new IllegalArgumentException("50%見積もりに0時間は設定できません.");
        this.estimate50Pct = estimate50Pct;
    }

    private void setEstimate90Pct(BigDecimal estimate90Pct) {
        if (estimate90Pct == null) throw new IllegalArgumentException("90%見積もりは必須です.");
        if (estimate90Pct.compareTo(BigDecimal.ZERO) == 0) throw new IllegalArgumentException("90%見積もりに0時間は設定できません.");
        this.estimate90Pct = estimate90Pct;
    }

    private void validate() {
        if (this.estimate50Pct.compareTo(this.estimate90Pct) > 0) {
            throw new IllegalArgumentException("50%見積もりが90%見積もりを超えています.");
        }
    }
}
