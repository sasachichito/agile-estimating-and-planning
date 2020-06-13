package com.github.sasachichito.agileplanning.domain.model.period;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void ResourcePeriodを生成できる() {
        try {
            new Period(
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 3, 31));

            new Period(
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 1, 1));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void 開始日が終了日より遅い場合IllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Period(
                    LocalDate.of(2020, 4, 1),
                    LocalDate.of(2020, 3, 31));
        });
    }
}