package com.github.sasachichito.agileplanning.domain.model.resource.entry.member.velocity;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class VelocityEstimate {
    private BigDecimal storyPoint;
    private BigDecimal cost;

    public VelocityEstimate(BigDecimal storyPoint, BigDecimal cost) {
        this.storyPoint = storyPoint;
        this.cost = cost;
    }
}
