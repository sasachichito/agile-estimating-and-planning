package com.github.sasachichito.agileplanning.domain.model.story;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

@Accessors(fluent = true)
@Getter
public class StoryTitle {

    private String title;

    public StoryTitle(String title) {
        this.setTitle(title);
    }

    private void setTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("StoryTitleは必須です.");
        }
        this.title = title;
    }
}
