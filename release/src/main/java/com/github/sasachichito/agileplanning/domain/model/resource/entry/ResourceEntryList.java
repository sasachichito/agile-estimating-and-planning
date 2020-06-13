package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.resource.VelocityIncrement;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;

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

    public boolean canFinishUp(ScopePoint scopePoint) {
        var left = scopePoint;

        for (var resourceEntry: this.resourceEntryList) {
            if(resourceEntry.canFinishUp(left)) {
                return true;
            }
            left = resourceEntry.howManyHoursLeft(left);
        }
        return false;
    }

    public boolean canFinishUpIn(ScopePoint scopePoint, Period period) {
        var left = scopePoint;

        for (var resourceEntry: this.resourceEntryList) {
            if(resourceEntry.canFinishUpIn(left, period)) {
                return true;
            }
            left = resourceEntry.howManyHoursLeft(left);
        }
        return false;
    }

    public Period periodForFinishUp(ScopePoint scopePoint) {
        var left = scopePoint;

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

    public VelocityIncrement storyPointIncrement(Period period) {
        VelocityIncrement velocityIncrement = new VelocityIncrement(new LinkedHashMap<>());
        int increment = 0;

        for (var resourceEntry: this.resourceEntryList) {
            VelocityIncrement merged = resourceEntry.storyPointIncrement(period, increment);
            velocityIncrement = velocityIncrement.merge(merged);
            increment = increment + resourceEntry.totalStoryPointFor(period);
        }
        return velocityIncrement;
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
