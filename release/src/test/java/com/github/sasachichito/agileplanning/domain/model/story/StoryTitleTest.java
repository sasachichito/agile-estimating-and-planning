package com.github.sasachichito.agileplanning.domain.model.story;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoryTitleTest {

    @Test
    void StoryTitleを生成できる() {
        try {
            new StoryTitle("Title");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void StoryTitleをNullで生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StoryTitle(null);
        });
    }

    @Test
    void StoryTitleを空文字で生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StoryTitle("");
        });
    }
}