package com.github.sasachichito.agileplanning.domain.model.story;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Accessors(fluent = true)
@Getter
public class StoryPoint {
    private int estimate50pct;
    private int estimate90pct;

    public StoryPoint(int estimate50pct, int estimate90pct) {
        this.setEstimate50pct(estimate50pct);
        this.setEstimate90pct(estimate90pct);
        this.validate();
    }

    BigDecimal anxietyVolume() {
        return BigDecimal.valueOf(this.estimate90pct)
                .subtract(BigDecimal.valueOf(this.estimate50pct))
                .pow(2, MathContext.DECIMAL64);
    }

    private void setEstimate50pct(int estimate50pct) {
        if (estimate50pct < 0) {
            throw new IllegalArgumentException("ストーリーポイントは負の値を許容しません.");
        }
        this.estimate50pct = estimate50pct;
    }

    private void setEstimate90pct(int estimate90pct) {
        if (estimate90pct < 0) {
            throw new IllegalArgumentException("ストーリーポイントは負の値を許容しません.");
        }
        this.estimate90pct = estimate90pct;
    }

    private void validate() {
        if (this.estimate50pct > this.estimate90pct) {
            throw new IllegalArgumentException("ストーリーポイントの50%見積もりは90見積もりよりも小さな値でなければなりません.");
        }
    }

    public BigDecimal simple() {
        BigDecimal sub = BigDecimal.valueOf(this.estimate90pct)
                .subtract(BigDecimal.valueOf(this.estimate50pct))
                .divide(BigDecimal.valueOf(2), 3, RoundingMode.DOWN);

        return BigDecimal.valueOf(this.estimate50pct).add(sub);
    }
}
