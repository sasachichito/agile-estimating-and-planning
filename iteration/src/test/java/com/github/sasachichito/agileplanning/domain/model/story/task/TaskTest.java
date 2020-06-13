package com.github.sasachichito.agileplanning.domain.model.story.task;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void Taskを生成できる() {
        try {
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(16));
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(8));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void nullでTaskを生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(10));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(new TaskId(1), null, BigDecimal.valueOf(8), BigDecimal.valueOf(10));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, null, BigDecimal.valueOf(8), BigDecimal.valueOf(10));
        });
    }

    @Test
    void Taskの見積もりに0時間を設定するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(0), BigDecimal.valueOf(2));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(2), BigDecimal.valueOf(0));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(0), BigDecimal.valueOf(0));
        });
    }

    @Test
    void Taskの50Pct見積もりが90Pct見積もりを超えるとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(new TaskId(1), new TaskName("TaskName"), BigDecimal.valueOf(8), BigDecimal.valueOf(7.9));
        });
    }
}