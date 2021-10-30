package com.github.sasachichito.agileplanning.domain.model.release;

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

    private ReleasePlanningService releasePlanningService;

    public void setIterationPlanningService(ReleasePlanningService releasePlanningService) {
        this.releasePlanningService = releasePlanningService;
    }

    @Override
    public void handleEvent(DomainEvent domainEvent) {
        domainEvent.subscribed(this);
    }

    @Override
    public void handle(StoryCreated storyCreated) {
//        無限ループとなる（Release ServiceからPUTリクエストが返ってくる）ため一旦処理なし
//        this.releasePlanningService.createStory(storyCreated.story());
    }

    @Override
    public void handle(StoryChanged storyChanged) {
//        無限ループとなる（Release ServiceからPUTリクエストが返ってくる）ため一旦処理なし
//        this.releasePlanningService.updateStory(storyChanged.story());
    }

    @Override
    public void handle(StoryRemoved storyRemoved) {
//        無限ループとなる（Release ServiceからPUTリクエストが返ってくる）ため一旦処理なし
//        this.releasePlanningService.deleteStory(storyRemoved.story());
    }

}
