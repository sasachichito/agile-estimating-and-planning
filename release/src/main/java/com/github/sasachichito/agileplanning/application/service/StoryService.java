package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.story.StoryCreateCmd;
import com.github.sasachichito.agileplanning.application.command.story.StoryUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.story.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class StoryService {

    private final StoryRepository storyRepository;

    public Story create(StoryCreateCmd storyCreateCmd) {
        Story story = new Story(
                this.storyRepository.nextStoryId(),
                new StoryTitle(storyCreateCmd.storyTitle()),
                new StoryPoint(storyCreateCmd.storyPoint().estimate50pct(), storyCreateCmd.storyPoint().estimate90pct()),
                storyCreateCmd.links());

        storyRepository.add(story);
        return story;
    }

    public Story get(int storyId) {
        return this.storyRepository.get(new StoryId(storyId));
    }

    public Set<Story> getAll() {
        return this.storyRepository.getAll();
    }

    public void delete(int storyId) {
        Story story = this.storyRepository.get(new StoryId(storyId));
        story.remove();
        this.storyRepository.put(story);
    }

    public Story updateOrPut(StoryUpdateCmd storyUpdateCmd) {
        StoryId storyId = new StoryId(storyUpdateCmd.storyId());

        if (this.storyRepository.exist(storyId)) {
            return this.update(
                    this.storyRepository.get(storyId),
                    storyUpdateCmd);
        }
        return this.put(storyUpdateCmd);
    }

    private Story put(StoryUpdateCmd storyUpdateCmd) {
        Story story = new Story(
                new StoryId(storyUpdateCmd.storyId()),
                new StoryTitle(storyUpdateCmd.storyTitle()),
                new StoryPoint(
                        storyUpdateCmd.storyPoint().estimate50pct(),
                        storyUpdateCmd.storyPoint().estimate90pct()),
                storyUpdateCmd.links());

        storyRepository.put(story);
        return story;
    }

    private Story update(Story story, StoryUpdateCmd storyUpdateCmd) {
        story.change(
                new StoryTitle(storyUpdateCmd.storyTitle()),
                new StoryPoint(
                        storyUpdateCmd.storyPoint().estimate50pct(),
                        storyUpdateCmd.storyPoint().estimate90pct()),
                storyUpdateCmd.links()
        );

        this.storyRepository.put(story);
        return story;
    }

    public BigDecimal storyPoint(StoryId storyId) {
        Story story = this.storyRepository.get(storyId);
        return story.simpleStoryPoint();
    }
}
