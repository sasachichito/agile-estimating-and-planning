package com.github.sasachichito.agileplanning.application.dpo;

import com.github.sasachichito.agileplanning.domain.model.burn.TotalBurnedHours;
import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@Builder
public class BurnUpChart {
    private ScopeIdealHours scopeIdealHours;
    private TotalBurnedHours totalBurnedHours;
    private WorkingHoursIncrement workingHoursIncrement;
}
