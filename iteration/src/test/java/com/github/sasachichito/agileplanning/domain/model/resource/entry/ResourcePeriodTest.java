package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ResourcePeriodTest {

    @Test
    void ResourcePeriodを生成できる() {
        try {
            new ResourcePeriod(
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 3, 31));

            new ResourcePeriod(
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 1, 1));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void 開始日が終了日より遅い場合IllegalArgumentExceptionを送出する() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ResourcePeriod(
                    LocalDate.of(2020, 4, 1),
                    LocalDate.of(2020, 3, 31));
        });
    }

    @ParameterizedTest
    @CsvSource({
            "2020-12-28, 2021-01-04, 2", // 年末年始
            "2021-01-04, 2021-01-04, 1", // 同日
            "2021-01-08, 2021-01-12, 2", // 土日祝日
            "2021-01-16, 2021-01-17, 0"  // 0日
    })
    void 勤務日数を算出できる(LocalDate start, LocalDate end, int expected) {
        ResourcePeriod resourcePeriod = new ResourcePeriod(start, end);
        assertEquals(expected, resourcePeriod.numOfWorkingDays());
    }
}