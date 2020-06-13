package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.resource.WorkingHoursIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class ResourceEntryList {
    private List<ResourceEntry> resourceEntryList;

    public ResourceEntryList(List<ResourceEntry> resourceEntryList) {
        this.setResourceEntryList(resourceEntryList);
    }

    public void provide(ResourceEntryListInterest resourceEntryListInterest) {
        resourceEntryListInterest.inform(this.resourceEntryList);
    }

    public boolean canFinishUp(ScopeIdealHours scopeIdealHours) {
        var left = scopeIdealHours;

        for (var resourceEntry: this.resourceEntryList) {
            if(resourceEntry.canFinishUp(left)) {
                return true;
            }
            left = resourceEntry.howManyHoursLeft(left);
        }
        return false;
    }

    public boolean canFinishUpIn(ScopeIdealHours scopeIdealHours, Period period) {
        var left = scopeIdealHours;

        for (var resourceEntry: this.resourceEntryList) {
            if(resourceEntry.canFinishUpIn(left, period)) {
                return true;
            }
            left = resourceEntry.howManyHoursLeft(left);
        }
        return false;
    }

    public Period periodForFinishUp(ScopeIdealHours scopeIdealHours) {
        var left = scopeIdealHours;

        for (var resourceEntry: this.resourceEntryList) {
            if(resourceEntry.canFinishUp(left)) {
                return new Period(this.startDay(), resourceEntry.finishUpDate(left));
            }
            left = resourceEntry.howManyHoursLeft(left);
        }
        throw new IllegalArgumentException();
    }

    private LocalDate startDay() {
        return resourceEntryList.get(0).startDay();
    }

    public WorkingHoursIncrement workingHoursIncrement(Period period) {
        WorkingHoursIncrement workingHoursIncrement = new WorkingHoursIncrement(new LinkedHashMap<>());
        BigDecimal increment = BigDecimal.ZERO;

        for (var resourceEntry: this.resourceEntryList) {
            WorkingHoursIncrement merged = resourceEntry.workingHoursIncrement(period, increment);
            increment = increment.add(resourceEntry.totalWorkingHoursFor(period));
            workingHoursIncrement = workingHoursIncrement.merge(merged);
        }
        return workingHoursIncrement;
    }

    public TotalCost totalCost(Period period) {
        TotalCost totalCost = new TotalCost(BigDecimal.ZERO);

        this.resourceEntryList.forEach(
                resourceEntry -> totalCost.merge(resourceEntry.totalCost(period)));

        return totalCost;
    }

    private void setResourceEntryList(List<ResourceEntry> resourceEntryList) {
        if (Objects.isNull(resourceEntryList)) throw new IllegalArgumentException("resourceEntryListは必須です.");
        if (resourceEntryList.isEmpty()) throw new IllegalArgumentException("空のresourceEntryListは許容されません.");

        LocalDate nextEntryStartDay = resourceEntryList.get(0).startDay();

        for (var resourceEntry : resourceEntryList) {
            if(!resourceEntry.startDay().equals(nextEntryStartDay)) {
                throw new IllegalArgumentException("ResourceEntryListの全期間は連続する必要があります.");
            }
            nextEntryStartDay = resourceEntry.endDay().plusDays(1);
        }
        this.resourceEntryList = resourceEntryList;
    }
}
