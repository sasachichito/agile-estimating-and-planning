package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Accessors(fluent = true)
@Getter
public class Burn {
    private BurnId burnId;
    private LocalDate date;
    private StoryId storyId;
    private boolean isRemoved = false;

    public Burn(
            BurnId burnId,
            LocalDate date,
            StoryId storyId,
            BurnSpec burnSpec
    ) {
        this.burnId = burnId;
        this.date = date;
        this.storyId = storyId;
        burnSpec.validate(this);
    }

    public BigDecimal burnPoint(Plan plan, BurnPointCalculator burnPointCalculator) {
        return burnPointCalculator.calculate(plan, this);
    }

    public void remove() {
        this.isRemoved = true;
    }

    public boolean isRelated(ScopeId scopeId, BurnRelationChecker burnRelationChecker) {
        return burnRelationChecker.isRelated(scopeId, this.storyId);
    }

    public boolean isAlreadyBurnedAt(LocalDate localDate) {
        return this.date.equals(localDate) || this.date.isBefore(localDate);
    }

    public void change(LocalDate date, StoryId storyId, BurnSpec burnSpec) {
        this.date = date;
        this.storyId = storyId;
        burnSpec.validate(this);
    }
}
