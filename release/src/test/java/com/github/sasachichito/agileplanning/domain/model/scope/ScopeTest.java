package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ScopeTest {

    @MockBean
    StoryRepository storyRepositoryMock;

    @Test
    void Scopeを生成できる() {
        when(this.storyRepositoryMock.exist(new StoryId(1))).thenReturn(true);
        when(this.storyRepositoryMock.exist(new StoryId(2))).thenReturn(true);

        try {
            new Scope(
                    new ScopeId(1), new ScopeTitle("scopeTitle"), List.of(
                            new StoryId(1), new StoryId(2)
            ), new ScopeSpec(this.storyRepositoryMock));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void nullでScopeを生成するとIllegalArgumentExceptionを送出する() {
        when(this.storyRepositoryMock.exist(new StoryId(1))).thenReturn(true);
        when(this.storyRepositoryMock.exist(new StoryId(2))).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(null, new ScopeTitle("scopeTitle"), List.of(
                    new StoryId(1), new StoryId(2)
            ), new ScopeSpec(this.storyRepositoryMock));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(new ScopeId(1), null, List.of(
                    new StoryId(1), new StoryId(2)
            ), new ScopeSpec(this.storyRepositoryMock));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(new ScopeId(1), new ScopeTitle("scopeTitle"), null, new ScopeSpec(this.storyRepositoryMock));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(null, null, null, new ScopeSpec(this.storyRepositoryMock));
        });
    }

    @Test
    void 空のStoryIdListでScopeを生成するとIllegalArgumentExceptionを送出する() {
        when(this.storyRepositoryMock.exist(new StoryId(1))).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(new ScopeId(1), new ScopeTitle("scopeTitle"), List.of(), new ScopeSpec(this.storyRepositoryMock));
        });
    }

    @Test
    void StoryIdListに重複があるとIllegalArgumentExceptionを送出する() {
        when(this.storyRepositoryMock.exist(new StoryId(1))).thenReturn(true);
        when(this.storyRepositoryMock.exist(new StoryId(2))).thenReturn(true);
        when(this.storyRepositoryMock.exist(new StoryId(3))).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(new ScopeId(1), new ScopeTitle("scopeTitle"), List.of(
                    new StoryId(1), new StoryId(1), new StoryId(3)
            ), new ScopeSpec(this.storyRepositoryMock));
        });
    }
}