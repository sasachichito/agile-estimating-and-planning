package com.github.sasachichito.agileplanning.domain.model.plan;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

@Accessors(fluent = true)
public class PlanTitle {
    @Getter
    private String title;

    public PlanTitle(String title) {
        this.setTitle(title);
    }

    private void setTitle(String title) {
        if (StringUtils.isEmpty(title)) throw new IllegalArgumentException("PlanTitleは必須かつ空文字を許容しません.");
        this.title = title;
    }
}
