package com.github.sasachichito.agileplanning.application.command.plan;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
@Getter
@Builder
public class PlanCreateCmd {
    private String planTitle;
    private int scopeId;
    private int resourceId;
}
