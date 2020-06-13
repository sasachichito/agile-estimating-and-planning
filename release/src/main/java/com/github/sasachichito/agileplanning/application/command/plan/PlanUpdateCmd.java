package com.github.sasachichito.agileplanning.application.command.plan;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
@Getter
@Builder
public class PlanUpdateCmd {
    private int planId;
    private String planTitle;
    private int scopeId;
    private int resourceId;
    private String periodStart;
    private String periodEnd;
}
