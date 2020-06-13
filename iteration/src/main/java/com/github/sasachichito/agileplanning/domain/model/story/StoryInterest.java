package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;

public interface StoryInterest {
    void inform(StoryId storyId);
    void inform(StoryTitle storyTitle);
    void inform(TaskList taskList);
}
