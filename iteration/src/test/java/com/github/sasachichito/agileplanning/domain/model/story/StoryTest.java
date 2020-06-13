package com.github.sasachichito.agileplanning.domain.model.story;

import com.github.sasachichito.agileplanning.domain.model.story.task.Task;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskList;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoryTest {

    @Test
    void Storyを生成できる() {
        try {
            new Story(
                    new StoryId(1),
                    new StoryTitle("TEST"),
                    new TaskList(List.of(
                            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(16))
                    )));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void Storyをnullで生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Story(null, null, null);
        });
    }
}