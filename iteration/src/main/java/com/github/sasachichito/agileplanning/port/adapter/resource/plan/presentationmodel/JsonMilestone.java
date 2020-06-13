package com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryInterest;
import com.github.sasachichito.agileplanning.domain.model.story.StoryTitle;
import com.github.sasachichito.agileplanning.domain.model.story.task.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JsonMilestone {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d(E)");
    private List<JsonStoryMilestone> milestoneList;

    public JsonMilestone(StoryMilestoneList storyMilestoneList, TaskMilestoneList taskMilestoneList) {
        this.milestoneList = storyMilestoneList.storyMilestoneList().stream()
                .map(storyMilestone ->
                        new JsonStoryMilestone(
                                storyMilestone.story(),
                                storyMilestone.localDate(),
                                taskMilestoneList.filterRelate(storyMilestone.story())))
                .collect(Collectors.toList());
    }

    @Getter
    public static class JsonStoryMilestone implements StoryInterest, TaskListInterest {
        private int storyId;
        private String storyTitle;
        private String milestone;
        private List<JsonTaskMilestone> taskList;

        public JsonStoryMilestone(Story story, LocalDate milestone, TaskMilestoneList taskMilestoneList) {
            story.provide(this);
            this.milestone = dtf.format(milestone);
            this.setTaskMilestoneList(taskMilestoneList);
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
        public void inform(List<Task> taskList) {}

        private void setTaskMilestoneList(TaskMilestoneList taskMilestoneList) {
            this.taskList = taskMilestoneList.taskMilestoneList().stream()
                    .map(taskMilestone ->
                            new JsonTaskMilestone(
                                    taskMilestone.taskAndRelatedStory().task(),
                                    taskMilestone.localDate()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class JsonTaskMilestone implements TaskInterest {
        private int taskId;
        private String taskName;
        private String milestone;

        JsonTaskMilestone(Task task, LocalDate milestone) {
            task.provide(this);
            this.milestone = dtf.format(milestone);
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
        public void inform50(BigDecimal estimate50Pct) {}

        @Override
        public void inform90(BigDecimal estimate90Pct) {}
    }
}
