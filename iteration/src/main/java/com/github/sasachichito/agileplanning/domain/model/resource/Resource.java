package com.github.sasachichito.agileplanning.domain.model.resource;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.ResourceEntryList;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceChanged;
import com.github.sasachichito.agileplanning.domain.model.resource.event.ResourceRemoved;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class Resource {
    @Getter
    private ResourceId resourceId;
    private ResourceEntryList resourceEntryList;
    @Getter
    private boolean isRemoved = false;

    public Resource(ResourceId resourceId, ResourceEntryList resourceEntryList) {
        this.resourceId = resourceId;
        this.resourceEntryList = resourceEntryList;
    }

    public Period periodForFinishUp(ScopeIdealHours scopeIdealHours) {
        return this.resourceEntryList.periodForFinishUp(scopeIdealHours);
    }

    public boolean canFinishUp(ScopeIdealHours scopeIdealHours) {
        return this.resourceEntryList.canFinishUp(scopeIdealHours);
    }

    public boolean canFinishUpIn(ScopeIdealHours scopeIdealHours, Period period) {
        return this.resourceEntryList.canFinishUpIn(scopeIdealHours, period);
    }

    public WorkingHoursIncrement increasedWorkingHours(Period period) {
        return this.resourceEntryList.workingHoursIncrement(period);
    }

    public void change(ResourceEntryList resourceEntryList) {
        // TODO 仕様クラスによるバリデーション
        this.resourceEntryList = resourceEntryList;
        DomainEventPublisher.instance().publish(new ResourceChanged(this));
    }

    public TotalCost totalCostIn(Period period) {
        return this.resourceEntryList.totalCost(period);
    }

    public void remove() {
        this.isRemoved = true;
        DomainEventPublisher.instance().publish(new ResourceRemoved(this));
    }

    public void provide(ResourceInterest resourceInterest) {
        resourceInterest.inform(this.resourceId);
        resourceInterest.inform(this.resourceEntryList);
    }
}
