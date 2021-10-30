package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.story.task.TaskId;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Accessors(fluent = true)
@Getter
public class Burn {
    private BurnId burnId;
    private LocalDate date;
    private TaskId taskId;
    private boolean isRemoved = false;

    public Burn(
            BurnId burnId,
            LocalDate date,
            TaskId taskId,
            BurnSpec burnSpec
    ) {
        this.burnId = burnId;
        this.date = date;
        this.taskId = taskId;
        burnSpec.validate(this);
    }

    public BigDecimal burnHours(Plan plan, BurnHoursCalculator burnHoursCalculator) {
        return burnHoursCalculator.calculate(plan, this);
    }

    public void remove() {
        this.isRemoved = true;
    }

    public boolean isRelated(ScopeId scopeId, BurnRelationChecker burnRelationChecker) {
        return burnRelationChecker.isRelated(scopeId, this.taskId);
    }

    public boolean isAlreadyBurnedAt(LocalDate localDate) {
        return this.date.equals(localDate) || this.date.isBefore(localDate);
    }

    public void change(LocalDate date, TaskId taskId, BurnSpec burnSpec) {
        this.date = date;
        this.taskId = taskId;
        burnSpec.validate(this);
    }
}
