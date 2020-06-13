package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity.Velocity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceEntryListTest {

    @Test
    void ResourceEntryListを生成できる() {
        try {
            new ResourceEntryList(List.of(
                    new ResourceEntry(
                            new ResourcePeriod(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31)),
                            new Velocity(3, BigDecimal.valueOf(15000))
                    ),
                    new ResourceEntry(
                            new ResourcePeriod(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 15)),
                            new Velocity(3, BigDecimal.valueOf(15000))
                    )
            ));
        } catch (Exception e) {
            fail();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2020-01-01, 2021-01-04, 2020-01-10, 2020-01-11, 2020-01-12, 2021-01-13",
            "2020-01-01, 2021-01-04, 2020-01-05, 2020-01-11, 2020-01-12, 2021-01-13",
            "2020-01-01, 2021-01-04, 2020-01-03, 2020-01-11, 2020-01-12, 2021-01-13",
            "2020-01-01, 2021-01-04, 2020-01-05, 2020-01-11, 2020-01-10, 2021-01-13"
    })
    void 期間の連続しないResourceEntryListを生成するとIllegalArgumentExceptionを送出する(
            LocalDate entry1Start, LocalDate entry1end,
            LocalDate entry2Start, LocalDate entry2end,
            LocalDate entry3Start, LocalDate entry3end
    ) {
        assertThrows(IllegalArgumentException.class, () -> {
            new ResourceEntryList(List.of(
                    new ResourceEntry(
                            new ResourcePeriod(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31)),
                            new Velocity(3, BigDecimal.valueOf(15000))
                    ),
                    new ResourceEntry(
                            new ResourcePeriod(LocalDate.of(2020, 2, 2), LocalDate.of(2020, 2, 15)),
                            new Velocity(3, BigDecimal.valueOf(15000))
                    )
            ));
        });
    }
}