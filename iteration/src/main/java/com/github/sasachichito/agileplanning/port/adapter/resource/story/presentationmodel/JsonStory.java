package com.github.sasachichito.agileplanning.port.adapter.resource.story.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.story.*;
import com.github.sasachichito.agileplanning.domain.model.story.task.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "ストーリーのJSON表現")
@Getter
public class JsonStory implements StoryInterest, TaskListInterest {
    @ApiModelProperty(value = "ストーリーID", example = "1", position = 1)
    private int storyId;

    @ApiModelProperty(value = "ストーリータイトル", example = "〇〇の更新をメール通知できる.", position = 2)
    private String storyTitle;

    @ApiModelProperty(position = 3)
    private List<JsonTask> taskList;

    public JsonStory(Story story) {
        story.provide(this);
    }

    @Override
    public void inform(StoryId storyId) {
        this.storyId = storyId.id();
    }

    @Override
    public void inform(StoryTitle storyTitle) {
        this.storyTitle = storyTitle.title();
    }

    @Override
    public void inform(TaskList taskList) {
        taskList.provide(this);
    }

    @Override
    public void inform(List<Task> taskList) {
        this.taskList = taskList.stream()
                .map(JsonTask::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class JsonTask implements TaskInterest {
        @ApiModelProperty(value = "タスクID", example = "1", position = 1)
        private int taskId;

        @ApiModelProperty(value = "タスク名", example = "現行システムの仕様調査", position = 2)
        private String taskName;

        @ApiModelProperty(value = "50%見積もり", example = "4", position = 3)
        private BigDecimal estimate50Pct;

        @ApiModelProperty(value = "90%見積もり", example = "10.5", position = 4)
        private BigDecimal estimate90Pct;

        JsonTask(Task task) {
            task.provide(this);
        }

        @Override
        public void inform(TaskId taskId) {
            this.taskId = taskId.id();
        }

        @Override
        public void inform(TaskName taskName) {
            this.taskName = taskName.name();
        }

        @Override
        public void inform50(BigDecimal estimate50Pct) {
            this.estimate50Pct = estimate50Pct;
        }

        @Override
        public void inform90(BigDecimal estimate90Pct) {
            this.estimate90Pct = estimate90Pct;
        }
    }
}
