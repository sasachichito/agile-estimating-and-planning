package com.github.sasachichito.agileplanning.application.command.story;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Accessors(fluent=true)
@Getter
@Builder
public class StoryCreateCmd {
    private String storyTitle;
    private List<Task> taskList;

    @Accessors(fluent=true)
    @Getter
    public static class Task {
        private String taskName;
        private BigDecimal estimate50Pct;
        private BigDecimal estimate90Pct;

        public Task(String taskName, BigDecimal estimate50Pct, BigDecimal estimate90Pct) {
            this.taskName = taskName;
            this.estimate50Pct = estimate50Pct;
            this.estimate90Pct = estimate90Pct;
        }
    }
}
