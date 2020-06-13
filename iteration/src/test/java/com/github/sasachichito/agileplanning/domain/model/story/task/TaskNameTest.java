package com.github.sasachichito.agileplanning.domain.model.story.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskNameTest {

    @Test
    void TaskNameを生成できる() {
        try {
            new TaskName("taskName");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void TaskNameにnullや空文字を設定するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskName(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TaskName("");
        });
    }
}