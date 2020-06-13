package com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.plan.milestone.MilestoneList;
import com.github.sasachichito.agileplanning.domain.model.story.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JsonMilestone {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d(E)");
    private List<JsonStoryMilestone> milestoneList;

    public JsonMilestone(MilestoneList milestoneList) {
        this.milestoneList = milestoneList.milestoneList().stream()
                .map(milestone -> new JsonStoryMilestone(milestone.story(), milestone.localDate()))
                .collect(Collectors.toList());
    }

    @Getter
    public static class JsonStoryMilestone implements StoryInterest {
        private int storyId;
        private String storyTitle;
        private String milestone;

        public JsonStoryMilestone(Story story, LocalDate milestone) {
            story.provide(this);
            this.milestone = dtf.format(milestone);
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
        public void inform(StoryPoint storyPoint) {}

        @Override
        public void informLinks(List<String> links) {}

    }
}
