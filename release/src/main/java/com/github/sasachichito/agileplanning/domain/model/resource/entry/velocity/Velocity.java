package com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class Velocity {
    private int storyPoint;
    private BigDecimal cost;

    public Velocity(int storyPoint, BigDecimal cost) {
        this.setStoryPoint(storyPoint);
        this.setCost(cost);
    }

    private void setStoryPoint(int storyPoint) {
        if (storyPoint < 0) throw new IllegalArgumentException("ベロシティのストーリーポイントは負の値を許容しません.");
        this.storyPoint = storyPoint;
    }

    private void setCost(BigDecimal cost) {
        if (cost.intValue() < 0) throw new IllegalArgumentException("ベロシティのコストは負の値を許容しません.");
        this.cost = cost;
    }
}
