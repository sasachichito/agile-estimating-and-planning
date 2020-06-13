package com.github.sasachichito.agileplanning.domain.model.story;

import java.util.List;

public interface StoryInterest {
    void inform(StoryId storyId);
    void inform(StoryTitle storyTitle);
    void inform(StoryPoint storyPoint);
    void informLinks(List<String> links);
}
