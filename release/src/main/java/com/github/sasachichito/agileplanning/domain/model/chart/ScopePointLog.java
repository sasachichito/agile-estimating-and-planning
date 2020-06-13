package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(fluent = true)
@Getter
public class ScopePointLog {
    private PlanId planId;
    private LocalDateTime dateTime;
    private ScopePoint scopePoint;
    private ChangeType changeType;

    public ScopePointLog(PlanId planId, LocalDateTime dateTime, ScopePoint scopePoint, ChangeType changeType) {
        this.planId = planId;
        this.dateTime = dateTime;
        this.scopePoint = scopePoint;
        this.changeType = changeType;
    }

    public boolean isInitialLog() {
        return this.changeType.equals(ChangeType.INITIAL);
    }

    public static enum ChangeType {
        INITIAL,
        NEW_STORY,
        DELETE_STORY,
        // TODO TBD
    }
}
