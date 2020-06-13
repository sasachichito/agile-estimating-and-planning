package com.github.sasachichito.agileplanning.port.adapter.resource.story.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Accessors(fluent=true)
@Getter
public class StoryRequest {
    @ApiModelProperty(value = "ストーリータイトル", example = "〇〇の更新をメール通知できる.", position = 1)
    public String storyTitle;

    @ApiModelProperty(position = 2)
    public List<TaskRequest> taskList;

    @Accessors(fluent=true)
    @Getter
    public static class TaskRequest {
        @ApiModelProperty(value = "タスク名", example = "現行システムの仕様調査", position = 1)
        public String taskName;

        @ApiModelProperty(value = "50%見積もり", example = "4", position = 2)
        public BigDecimal estimate50Pct;

        @ApiModelProperty(value = "90%見積もり", example = "10.5", position = 3)
        public BigDecimal estimate90Pct;
    }
}
