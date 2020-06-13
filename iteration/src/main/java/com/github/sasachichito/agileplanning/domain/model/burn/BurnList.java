package com.github.sasachichito.agileplanning.domain.model.burn;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDay;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDayListGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BurnList {
    private List<Burn> burnList;

    public BurnList(List<Burn> burnList) {
        this.burnList = burnList;
    }

    public TotalBurnedHours totalBurnHours(BurnHoursCalculator burnHoursCalculator) {
        BigDecimal totalBurnHours = this.burnList.stream()
                .map(burn -> burn.burnHours(burnHoursCalculator))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TotalBurnedHours(totalBurnHours);
    }

    public BurnIncrement burnIncrement(Period period, BurnHoursCalculator burnHoursCalculator) {
        List<WorkDay> workDayList = WorkDayListGenerator.exec(period.start(), period.end());

        Map<LocalDate, BigDecimal> burnIncrementMap = new LinkedHashMap<>();

        workDayList.forEach(workDay -> {
            BigDecimal totalBurnPointAtDay = this.burnList.stream()
                    .filter(burn -> burn.isAlreadyBurnedAt(workDay.localDate()))
                    .map(burn -> burn.burnHours(burnHoursCalculator))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            burnIncrementMap.put(workDay.localDate(), totalBurnPointAtDay);
        });

        return new BurnIncrement(burnIncrementMap);
    }
}
