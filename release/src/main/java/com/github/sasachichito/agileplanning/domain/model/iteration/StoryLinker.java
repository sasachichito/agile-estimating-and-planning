package com.github.sasachichito.agileplanning.domain.model.iteration;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEvent;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryChanged;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryCreated;
import com.github.sasachichito.agileplanning.domain.model.story.event.StoryRemoved;

public class StoryLinker implements
        StoryCreated.Subscriber,
        StoryChanged.Subscriber,
        StoryRemoved.Subscriber
{

    private static final StoryLinker STORY_LINKER = new StoryLinker();

    public static StoryLinker instance() {
        return STORY_LINKER;
    }

    private StoryLinker() {}

    private IterationPlanningService iterationPlanningService;

    public void setIterationPlanningService(IterationPlanningService iterationPlanningService) {
        this.iterationPlanningService = iterationPlanningService;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(StoryCreated storyCreated) {
        this.iterationPlanningService.createStory(storyCreated.story());
    }

    @Override
    public void handle(StoryChanged storyChanged) {
        this.iterationPlanningService.updateStory(storyChanged.story());
    }

    @Override
    public void handle(StoryRemoved storyRemoved) {
        this.iterationPlanningService.deleteStory(storyRemoved.story());
    }
}
