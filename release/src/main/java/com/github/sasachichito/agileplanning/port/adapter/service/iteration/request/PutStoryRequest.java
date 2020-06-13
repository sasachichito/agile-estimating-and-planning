package com.github.sasachichito.agileplanning.port.adapter.service.iteration.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sasachichito.agileplanning.domain.model.story.*;

import java.util.List;

public class PutStoryRequest implements StoryInterest {

    public int storyId;
    public String storyTitle;
    public List taskList = List.of();

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
    public void inform(StoryPoint storyPoint) {}

    @Override
    public void informLinks(List<String> links) {}

    public String json() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json生成失敗: " + e.getMessage());
        }
    }
}
