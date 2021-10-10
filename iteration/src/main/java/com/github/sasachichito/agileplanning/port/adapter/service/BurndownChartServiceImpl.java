package com.github.sasachichito.agileplanning.port.adapter.service;

import com.github.sasachichito.agileplanning.domain.model.burn.*;
import com.github.sasachichito.agileplanning.domain.model.chart.*;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDay;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDayListGenerator;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BurndownChartServiceImpl implements BurndownChartService {

    private final BurnRepository burnRepository;
    private final PlanRepository planRepository;
    private final ResourceRepository resourceRepository;
    private final ScopeRepository scopeRepository;
    private final StoryRepository storyRepository;
    private final ScopeIdealHoursLogRepository scopeIdealHoursLogRepository;
    private final ChartRepository chartRepository;

    @Override
    public void saveChart(Plan plan, ScopeIdealHours scopeIdealHours) {
        int nextChartVersion = 1;

        if (this.chartRepository.exist(plan.planId())) {
            BurndownLineChart lastVersion = this.chartRepository.getLastVersion(plan.planId());
            nextChartVersion = lastVersion.version() + 1;
        }

        List<WorkDay> workDayList = WorkDayListGenerator.exec(plan.period().start(), plan.period().end());
        WorkDay previousWorkDay = WorkDayListGenerator.previousWorkDayOf(plan.period().start());

        ScopeIdealHoursLogList scopeIdealHoursLogList = new ScopeIdealHoursLogList(
                this.scopeIdealHoursLogRepository.get(plan.planId()));

        List<LocalDate> period = getPeriod(workDayList, previousWorkDay);

        List<BigDecimal> changedPlan = getChangedPlan(
                plan,
                this.resourceRepository.get(plan.resourceId()),
                workDayList,
                previousWorkDay,
                scopeIdealHoursLogList);

        var burndownLineChart = new BurndownLineChart(
                plan.planId(),
                nextChartVersion,
                LocalDateTime.now(),
                scopeIdealHours,
                period,
                changedPlan);

        this.chartRepository.add(burndownLineChart);
    }

    @Override
    public BurndownLineChartList getLineCharts(PlanId planId) {
        if (!this.chartRepository.exist(planId)) {
            throw new ResourceNotFoundException("planId " + planId.id() + " is not found");
        }

        Plan plan = this.planRepository.get(planId);

        List<Burn> aBurnList = this.burnRepository.getAll().stream()
                .filter(burn -> burn.isRelated(
                        plan.scopeId(),
                        new BurnRelationChecker(this.scopeRepository, this.storyRepository)))
                .collect(Collectors.toList());

        BurnList burnList = new BurnList(aBurnList);

        BurndownLineChartList burndownLineChartList = new BurndownLineChartList(this.chartRepository.getList(planId));
        burndownLineChartList.setActualResult(plan, burnList, new BurnHoursCalculator(this.scopeRepository, this.storyRepository));

        return burndownLineChartList;
    }

    public List<BurndownLineChart> getLineCharts() {
        return this.chartRepository.getAll();
    }

    private List<LocalDate> getPeriod(List<WorkDay> workDayList, WorkDay previousWorkDay) {
        List<LocalDate> period = workDayList.stream()
                .map(WorkDay::localDate)
                .collect(Collectors.toList());
        period.add(0, previousWorkDay.localDate());
        return period;
    }

    private List<BigDecimal> getChangedPlan(Plan plan, Resource resource, List<WorkDay> workDayList, WorkDay previousWorkDay, ScopeIdealHoursLogList scopeIdealHoursLogList) {
        List<BigDecimal> changedPlan = workDayList.stream()
                .map(workDay -> scopeIdealHoursLogList.expectedLeftHours(
                        workDay.localDate(), resource.increasedWorkingHours(plan.period())
                ))
                .collect(Collectors.toList());
        changedPlan.add(0, scopeIdealHoursLogList.hoursAt(previousWorkDay.localDate()));
        return changedPlan;
    }
}
