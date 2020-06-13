package com.github.sasachichito.agileplanning.domain.model.story.task;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    void TaskListを生成できる() {
        try {
            new TaskList(List.of(
                    new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(16))
            ));
            new TaskList(List.of());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void TaskListをnullで生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskList(null);
        });
    }
}