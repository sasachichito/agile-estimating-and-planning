package com.github.sasachichito.agileplanning.domain.model.iteration;

import com.github.sasachichito.agileplanning.domain.model.story.Story;

public interface IterationPlanningService {
    void createStory(Story story);
    void updateStory(Story story);
    void deleteStory(Story story);
}
