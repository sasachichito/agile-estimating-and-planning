package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.iteration.IterationPlanningService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StoryTest {

    @MockBean
    IterationPlanningService iterationPlanningService;

    @Test
    void Storyを生成できる() {
        try {
            Story story = new Story(
                    new StoryId(1),
                    new StoryTitle("TEST"),
                    new StoryPoint(3, 4),
                    List.of("http://~"));

            verify(this.iterationPlanningService, times(1)).createStory(story);
            verify(this.iterationPlanningService, never()).updateStory(story);
            verify(this.iterationPlanningService, never()).deleteStory(story);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void Storyをnullで生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Story(null, null, null, null);
        });
    }
}