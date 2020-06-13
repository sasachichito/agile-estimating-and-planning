package com.github.sasachichito.agileplanning.port.adapter.service;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopeIdealHoursLogList;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDay;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDayListGenerator;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BurndownChartServiceImpl implements BurndownChartService {

    private HashMap<PlanId, List<ScopeIdealHoursLog>> scopeChangeLogMap = new HashMap<>();

    @Override
    public List<ScopeIdealHoursLog> get(PlanId planId) {
        return this.scopeChangeLogMap.getOrDefault(planId, List.of());
    }

    @Override
    public List<ScopeIdealHoursLog> getAll() {
        return this.scopeChangeLogMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ScopeIdealHoursLog scopeIdealHoursLog) {
        this.scopeChangeLogMap.computeIfAbsent(
                scopeIdealHoursLog.planId(),
                (planId) -> new ArrayList<>());

        this.scopeChangeLogMap.get(scopeIdealHoursLog.planId())
                .add(scopeIdealHoursLog);
    }

    @Override
    public BurndownLineChart getLineChart(Plan plan, Resource resource, BurnIncrement burnIncrement) {
        List<WorkDay> workDayList = WorkDayListGenerator.exec(plan.period().start(), plan.period().end());
        WorkDay previousWorkDay = WorkDayListGenerator.previousWorkDayOf(plan.period().start());

        ScopeIdealHoursLogList scopeIdealHoursLogList = new ScopeIdealHoursLogList(
                this.get(plan.planId()));

        List<LocalDate> period = getPeriod(workDayList, previousWorkDay);

        List<BigDecimal> initialPlan = getInitialPlan(plan, resource, workDayList, scopeIdealHoursLogList);

        List<BigDecimal> changedPlan = getChangedPlan(plan, resource, workDayList, previousWorkDay, scopeIdealHoursLogList);

        List<BigDecimal> actualResult = getActualResult(burnIncrement, workDayList, previousWorkDay, scopeIdealHoursLogList);

        return new BurndownLineChart(period, initialPlan, changedPlan, actualResult);
    }

    private List<LocalDate> getPeriod(List<WorkDay> workDayList, WorkDay previousWorkDay) {
        List<LocalDate> period = workDayList.stream()
                .map(WorkDay::localDate)
                .collect(Collectors.toList());
        period.add(0, previousWorkDay.localDate());
        return period;
    }

    private List<BigDecimal> getInitialPlan(Plan plan, Resource resource, List<WorkDay> workDayList, ScopeIdealHoursLogList scopeIdealHoursLogList) {
        List<BigDecimal> initialPlan = workDayList.stream()
                .map(workDay -> scopeIdealHoursLogList.expectedLeftHoursIgnoreChange(
                        workDay.localDate(), resource.increasedWorkingHours(plan.period())
                ))
                .collect(Collectors.toList());
        initialPlan.add(0, scopeIdealHoursLogList.initialHours());
        return initialPlan;
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

    private List<BigDecimal> getActualResult(BurnIncrement burnIncrement, List<WorkDay> workDayList, WorkDay previousWorkDay, ScopeIdealHoursLogList scopeIdealHoursLogList) {
        List<BigDecimal> actualResult = workDayList.stream()
                .filter(workDay ->
                        workDay.localDate().isBefore(LocalDate.now())
                                || workDay.localDate().equals(LocalDate.now()))
                .map(workDay -> scopeIdealHoursLogList.resultLeftHours(
                        workDay.localDate(), burnIncrement
                ))
                .collect(Collectors.toList());
        actualResult.add(0, scopeIdealHoursLogList.hoursAt(previousWorkDay.localDate()));
        return actualResult;
    }

    @Override
    public void flash() {
        this.scopeChangeLogMap = new HashMap<>();
    }
}
