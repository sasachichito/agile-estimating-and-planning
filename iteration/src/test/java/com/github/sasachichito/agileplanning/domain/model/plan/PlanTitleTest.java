package com.github.sasachichito.agileplanning.domain.model.plan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanTitleTest {

    @Test
    void PlanTitleを生成できる() {
        try {
            new PlanTitle("title");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void PlanTitleをnullや空文字で生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PlanTitle(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PlanTitle("");
        });
    }
}