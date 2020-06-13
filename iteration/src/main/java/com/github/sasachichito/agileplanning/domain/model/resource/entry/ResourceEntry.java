package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.MemberSet;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ResourceEntry {
    private ResourcePeriod resourcePeriod;
    private MemberSet memberSet;

    public ResourceEntry(ResourcePeriod resourcePeriod, MemberSet memberSet) {
        this.resourcePeriod = resourcePeriod;
        this.memberSet = memberSet;
    }

    public void provide(ResourceEntryInterest resourceEntryInterest) {
        resourceEntryInterest.inform(this.resourcePeriod);
        resourceEntryInterest.inform(this.memberSet);
    }

    boolean canFinishUp(ScopeIdealHours scopeIdealHours) {
        return this.memberSet.totalWorkingHoursPerDay().hours()
                .multiply(BigDecimal.valueOf(this.resourcePeriod.numOfWorkingDays()))
                .compareTo(scopeIdealHours.hours()) >= 0;
    }

    boolean canFinishUpIn(ScopeIdealHours scopeIdealHours, Period period) {
        return this.memberSet.totalWorkingHoursPerDay().hours()
                .multiply(BigDecimal.valueOf(this.resourcePeriod.numOfWorkingDaysIn(period)))
                .compareTo(scopeIdealHours.hours()) >= 0;
    }

    ScopeIdealHours howManyHoursLeft(ScopeIdealHours scopeIdealHours) {
        BigDecimal totalWork = this.memberSet.totalWorkingHoursPerDay().hours()
                .multiply(BigDecimal.valueOf(this.resourcePeriod.numOfWorkingDays()));
        return new ScopeIdealHours(scopeIdealHours.hours().subtract(totalWork));
    }

    LocalDate finishUpDate(ScopeIdealHours scopeIdealHours) {
        int requiredDays = IntStream.rangeClosed(1, this.resourcePeriod.numOfWorkingDays())
                .filter(i -> BigDecimal.valueOf(i).multiply(this.memberSet.totalWorkingHoursPerDay().hours())
                        .compareTo(scopeIdealHours.hours()) >= 0)
                .findFirst().orElseThrow(IllegalArgumentException::new);

        return this.resourcePeriod.nthWorkDay(requiredDays);
    }

    LocalDate startDay() {
        return this.resourcePeriod.start();
    }

    LocalDate endDay() { return this.resourcePeriod.end(); }

    WorkingHoursIncrement workingHoursIncrement(Period period, BigDecimal increment) {
        var workdayList = this.resourcePeriod.workDayListFor(period);
        var totalWorkingHoursPerDay = this.memberSet.totalWorkingHoursPerDay();

        Map<LocalDate, WorkingHoursIncrement.WorkingHours> workingHoursMap = new LinkedHashMap<>();

        for (var workday : workdayList) {
            increment = increment.add(totalWorkingHoursPerDay.hours());
            workingHoursMap.put(workday.localDate(), new WorkingHoursIncrement.WorkingHours(increment));
        }
        return new WorkingHoursIncrement(workingHoursMap);
    }

    BigDecimal totalWorkingHoursFor(Period period) {
        var workdayList = this.resourcePeriod.workDayListFor(period);
        var totalWorkingHoursPerDay = this.memberSet.totalWorkingHoursPerDay();
        return BigDecimal.valueOf(workdayList.size()).multiply(totalWorkingHoursPerDay.hours());
    }

    TotalCost totalCost(Period period) {
        var workdayList = this.resourcePeriod.workDayListFor(period);
        var totalUnitCostPerDay = this.memberSet.totalUnitCostPerDay();

        return new TotalCost(
                totalUnitCostPerDay.price().multiply(
                        BigDecimal.valueOf(workdayList.size())));
    }
}
