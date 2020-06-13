package com.github.sasachichito.agileplanning.domain.model.scope;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTitleTest {

    @Test
    void ScopeTitleを生成できる() {
        try {
            new ScopeTitle("scopeTitle");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void ScopeTitleをnullと空文字で生成するとIllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ScopeTitle(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new ScopeTitle("");
        });
    }
}