package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity.Velocity;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ResourceEntry {
    private ResourcePeriod resourcePeriod;
    private Velocity velocity;

    public ResourceEntry(ResourcePeriod resourcePeriod, Velocity velocity) {
        this.resourcePeriod = resourcePeriod;
        this.velocity = velocity;
    }

    public void provide(ResourceEntryInterest resourceEntryInterest) {
        resourceEntryInterest.inform(this.resourcePeriod);
        resourceEntryInterest.inform(this.velocity);
    }

    boolean canFinishUp(ScopePoint scopePoint) {
        return BigDecimal.valueOf(this.velocity.storyPoint())
                .multiply(BigDecimal.valueOf(this.resourcePeriod.numOfWorkingDays()))
                .compareTo(scopePoint.point()) >= 0;
    }

    boolean canFinishUpIn(ScopePoint scopePoint, Period period) {
        return BigDecimal.valueOf(this.velocity.storyPoint())
                .multiply(BigDecimal.valueOf(this.resourcePeriod.numOfWorkingDaysIn(period)))
                .compareTo(scopePoint.point()) >= 0;
    }

    ScopePoint howManyHoursLeft(ScopePoint scopePoint) {
        int totalPoint = this.velocity.storyPoint() * this.resourcePeriod.numOfWorkingDays();
        return new ScopePoint(scopePoint.point().subtract(BigDecimal.valueOf(totalPoint)));
    }

    LocalDate finishUpDate(ScopePoint scopePoint) {
        int requiredDays = IntStream.rangeClosed(1, this.resourcePeriod.numOfWorkingDays())
                .filter(i -> BigDecimal.valueOf(i).multiply(BigDecimal.valueOf(this.velocity.storyPoint()))
                        .compareTo(scopePoint.point()) >= 0)
                .findFirst().orElseThrow(IllegalArgumentException::new);

        return this.resourcePeriod.nthWorkDay(requiredDays);
    }

    LocalDate startDay() {
        return this.resourcePeriod.start();
    }

    LocalDate endDay() { return this.resourcePeriod.end(); }

    VelocityIncrement storyPointIncrement(Period period, int increment) {
        var workdayList = this.resourcePeriod.workDayListFor(period);
        int storyPoint = this.velocity.storyPoint();

        Map<LocalDate, Integer> incrementedStoryPoint = new LinkedHashMap<>();

        for (var workday : workdayList) {
            increment = increment + storyPoint;
            incrementedStoryPoint.put(workday.localDate(), increment);
        }
        return new VelocityIncrement(incrementedStoryPoint);
    }

    int totalStoryPointFor(Period period) {
        var workdayList = this.resourcePeriod.workDayListFor(period);
        return workdayList.size() * this.velocity.storyPoint();
    }

    TotalCost totalCost(Period period) {
        var workdayList = this.resourcePeriod.workDayListFor(period);

        return new TotalCost(
                this.velocity.cost().multiply(BigDecimal.valueOf(workdayList.size()))
        );
    }
}
