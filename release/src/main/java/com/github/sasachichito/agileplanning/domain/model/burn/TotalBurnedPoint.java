package com.github.sasachichito.agileplanning.domain.model.burn;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class TotalBurnedPoint {
    private BigDecimal point;

    public TotalBurnedPoint(BigDecimal point) {
        this.point = point;
    }
}
