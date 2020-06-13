package com.github.sasachichito.agileplanning.domain.model.scope;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Accessors(fluent = true)
public class ScopeTitle {
    @Getter
    private String title;

    public ScopeTitle(String title) {
        this.setTitle(title);
    }

    private void setTitle(String title) {
        if (StringUtils.isEmpty(title)) throw new IllegalArgumentException("ScopeTitleは必須です.");
        this.title = title;
    }
}
