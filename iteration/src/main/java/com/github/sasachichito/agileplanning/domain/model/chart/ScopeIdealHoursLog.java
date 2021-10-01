package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(fluent = true)
@Getter
public class ScopeIdealHoursLog {
    private PlanId planId;
    private LocalDateTime dateTime;
    private ScopeIdealHours scopeIdealHours;
    private ChangeType changeType;

    public ScopeIdealHoursLog(PlanId planId, LocalDateTime dateTime, ScopeIdealHours scopeIdealHours, ChangeType changeType) {
        this.planId = planId;
        this.dateTime = dateTime;
        this.scopeIdealHours = scopeIdealHours;
        this.changeType = changeType;
    }

    public boolean isInitialLog() {
        return this.changeType.equals(ChangeType.INITIAL);
    }

    public static enum ChangeType {
        INITIAL,
        SCOPE_CHANGED,
        RESOURCE_CHANGED,
        DELETE_STORY,
        // TODO TBD
    }
}
