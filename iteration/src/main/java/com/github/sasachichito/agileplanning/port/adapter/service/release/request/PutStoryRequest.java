package com.github.sasachichito.agileplanning.port.adapter.service.release.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryInterest;
import com.github.sasachichito.agileplanning.domain.model.story.StoryTitle;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;

import java.util.List;

public class PutStoryRequest implements StoryInterest {

    public int storyId;
    public String storyTitle;
    public PutStoryRequest.StoryPoint storyPoint = new StoryPoint();
    public List links = List.of();

    public PutStoryRequest(Story story) {
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

    }

    public static class StoryPoint {
        public int estimate50pct = 1;
        public int estimate90pct = 1;
    }

    public String json() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json生成失敗: " + e.getMessage());
        }
    }
}
