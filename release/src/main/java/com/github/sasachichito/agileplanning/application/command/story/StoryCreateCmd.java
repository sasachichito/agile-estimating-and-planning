package com.github.sasachichito.agileplanning.application.command.story;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent=true)
@Getter
@Builder
public class StoryCreateCmd {
    private String storyTitle;
    private StoryPoint storyPoint;
    private List<String> links;

    @Accessors(fluent=true)
    @Getter
    public static class StoryPoint {
        private int estimate50pct;
        private int estimate90pct;

        public StoryPoint(int estimate50pct, int estimate90pct) {
            this.estimate50pct = estimate50pct;
            this.estimate90pct = estimate90pct;
        }
    }
}
