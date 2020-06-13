package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.plan.PlanCreateCmd;
import com.github.sasachichito.agileplanning.application.command.plan.PlanUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.*;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCostCalculator;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.MilestoneService;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PlanService {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PlanRepository planRepository;
    private final ScopeRepository scopeRepository;
    private final StoryRepository storyRepository;
    private final ResourceRepository resourceRepository;

    public Plan create(PlanCreateCmd planCreateCmd) {
        PlanFactory planFactory = new PlanFactory(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        Plan plan = planFactory.create(
                this.planRepository.nextId(),
                new PlanTitle(planCreateCmd.planTitle()),
                new ScopeId(planCreateCmd.scopeId()),
                new ResourceId(planCreateCmd.resourceId()));

        this.planRepository.add(plan);
        return plan;
    }

    public TaskMilestoneList generateTaskMilestoneList(PlanId planId) {
        Plan plan = this.planRepository.get(planId);

        MilestoneService milestoneService = new MilestoneService(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        return plan.taskMilestoneList(milestoneService);
    }

    public StoryMilestoneList generateStoryMilestoneList(PlanId planId) {
        Plan plan = this.planRepository.get(planId);

        MilestoneService milestoneService = new MilestoneService(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        return plan.storyMilestoneList(milestoneService);
    }

    public TotalCost calculateTotalCost(PlanId planId) {
        Plan plan = this.planRepository.get(planId);

        return plan.totalCost(
                new TotalCostCalculator(this.resourceRepository));
    }

    public Set<Plan> getAll() {
        return this.planRepository.getAll();
    }

    public Plan updateOrPut(PlanUpdateCmd planUpdateCmd) {
        PlanId planId = new PlanId(planUpdateCmd.planId());

        if (this.planRepository.exist(planId)) {
            return this.update(
                    this.planRepository.get(planId),
                    planUpdateCmd);
        }
        return this.put(planUpdateCmd);
    }

    private Plan put(PlanUpdateCmd planUpdateCmd) {
        PlanFactory planFactory = new PlanFactory(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        Plan plan = planFactory.create(
                new PlanId(planUpdateCmd.planId()),
                new PlanTitle(planUpdateCmd.planTitle()),
                new ScopeId(planUpdateCmd.scopeId()),
                new ResourceId(planUpdateCmd.resourceId()), new Period(
                        LocalDate.parse(planUpdateCmd.periodStart(),dtf),
                        LocalDate.parse(planUpdateCmd.periodEnd(), dtf)
                ));

        this.planRepository.put(plan);
        return plan;
    }

    private Plan update(Plan plan, PlanUpdateCmd planUpdateCmd) {
        PlanFactory planFactory = new PlanFactory(
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        planFactory.change(
                plan,
                new PlanTitle(planUpdateCmd.planTitle()),
                new ScopeId(planUpdateCmd.scopeId()),
                new ResourceId(planUpdateCmd.resourceId()),
                new Period(
                        LocalDate.parse(planUpdateCmd.periodStart(),dtf),
                        LocalDate.parse(planUpdateCmd.periodEnd(), dtf)
                ));

        this.planRepository.put(plan);
        return plan;
    }

    public Plan getPlan(PlanId planId) {
        return this.planRepository.get(planId);
    }

    public void delete(PlanId planId) {
        Plan plan = this.planRepository.get(planId);
        plan.remove();
        this.planRepository.put(plan);
    }
}
