package com.github.sasachichito.agileplanning.application.dpo;

import com.github.sasachichito.agileplanning.domain.model.burn.TotalBurnedPoint;
import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@Builder
public class BurnUpChart {
    private ScopePoint scopePoint;
    private TotalBurnedPoint totalBurnedPoint;
    private VelocityIncrement velocityIncrement;
}
