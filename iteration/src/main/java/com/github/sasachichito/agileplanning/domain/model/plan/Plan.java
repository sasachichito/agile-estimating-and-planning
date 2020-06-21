package com.github.sasachichito.agileplanning.domain.model.plan;

import com.github.sasachichito.agileplanning.domain.model.event.DomainEventPublisher;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.period.PeriodCalculator;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCostCalculator;
import com.github.sasachichito.agileplanning.domain.model.plan.event.PlanCreated;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.MilestoneService;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Accessors(fluent = true)
public class Plan {

    private PlanId planId;
    private PlanTitle planTitle;
    private ScopeId scopeId;
    private ResourceId resourceId;
    private Period period;
    private boolean isRemoved = false;

    Plan(PlanId planId, PlanTitle planTitle, ScopeId scopeId, ResourceId resourceId, Period period) {
        this.setPlanId(planId);
        this.setPlanTitle(planTitle);
        this.setScopeId(scopeId);
        this.setResourceId(resourceId);
        this.setPeriod(period);

        DomainEventPublisher.instance().publish(new PlanCreated(this));
    }

    void change(PlanTitle planTitle, ScopeId scopeId, ResourceId resourceId, Period period) {
        this.setPlanTitle(planTitle);
        this.setScopeId(scopeId);
        this.setResourceId(resourceId);
        this.setPeriod(period);
    }

    public boolean hasScope(ScopeId scopeId) {
        return this.scopeId.equals(scopeId);
    }

    public boolean hasResource(ResourceId resourceId) {
        return this.resourceId.equals(resourceId);
    }

    public void adjustPeriod(PeriodCalculator periodCalculator) {
        try {
            this.period = periodCalculator.exec(this.scopeId, this.resourceId);
        } catch (IllegalArgumentException e) {
            this.remove();
            throw new IllegalArgumentException(e.getMessage() + " プランID " + this.planId.id() + " を削除します.");
        }
    }

    public TaskMilestoneList taskMilestoneList(MilestoneService milestoneService) {
        return milestoneService.generateTaskMilestone(this);
    }

    public StoryMilestoneList storyMilestoneList(MilestoneService milestoneService) {
        return milestoneService.generateStoryMilestone(this);
    }

    public TotalCost totalCost(TotalCostCalculator totalCostCalculator) {
        return totalCostCalculator.calc(this);
    }

    public void remove() {
        this.isRemoved = true;
    }

    private void setPlanId(PlanId planId) {
        if (Objects.isNull(planId)) throw new IllegalArgumentException("PlanIdは必須です.");
        this.planId = planId;
    }

    private void setPlanTitle(PlanTitle planTitle) {
        if (Objects.isNull(planTitle)) throw new IllegalArgumentException("PlanTitleは必須です.");
        this.planTitle = planTitle;
    }

    private void setScopeId(ScopeId scopeId) {
        if (Objects.isNull(scopeId)) throw new IllegalArgumentException("ScopeIdは必須です.");
        this.scopeId = scopeId;
    }

    private void setResourceId(ResourceId resourceId) {
        if (Objects.isNull(resourceId)) throw new IllegalArgumentException("ResourceIdは必須です.");
        this.resourceId = resourceId;
    }

    private void setPeriod(Period period) {
        if (Objects.isNull(period)) throw new IllegalArgumentException("periodは必須です.");
        this.period = period;
    }
}
