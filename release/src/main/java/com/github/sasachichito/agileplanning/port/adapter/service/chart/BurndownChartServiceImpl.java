package com.github.sasachichito.agileplanning.port.adapter.service.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLogList;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDay;
import com.github.sasachichito.agileplanning.domain.model.period.WorkDayListGenerator;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BurndownChartServiceImpl implements BurndownChartService {

    private HashMap<PlanId, List<ScopePointLog>> scopeChangeLogMap = new HashMap<>();

    @Override
    public List<ScopePointLog> get(PlanId planId) {
        return this.scopeChangeLogMap.getOrDefault(planId, List.of());
    }

    @Override
    public List<ScopePointLog> getAll() {
        return this.scopeChangeLogMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ScopePointLog scopePointLog) {
        this.scopeChangeLogMap.computeIfAbsent(
                scopePointLog.planId(),
                (planId) -> new ArrayList<>());

        this.scopeChangeLogMap.get(scopePointLog.planId())
                .add(scopePointLog);
    }

    @Override
    public BurndownLineChart getLineChart(Plan plan, Resource resource, BurnIncrement burnIncrement) {
        List<WorkDay> workDayList = WorkDayListGenerator.exec(plan.period().start(), plan.period().end());
        WorkDay previousWorkDay = WorkDayListGenerator.previousWorkDayOf(plan.period().start());

        ScopePointLogList scopePointLogList = new ScopePointLogList(
                this.get(plan.planId()));

        List<LocalDate> period = getPeriod(workDayList, previousWorkDay);

        List<BigDecimal> initialPlan = getInitialPlan(plan, resource, workDayList, scopePointLogList);

        List<BigDecimal> changedPlan = getChangedPlan(plan, resource, workDayList, previousWorkDay, scopePointLogList);

        List<BigDecimal> actualResult = getActualResult(burnIncrement, workDayList, previousWorkDay, scopePointLogList);

        return new BurndownLineChart(period, initialPlan, changedPlan, actualResult);
    }

    private List<BigDecimal> getActualResult(BurnIncrement burnIncrement, List<WorkDay> workDayList, WorkDay previousWorkDay, ScopePointLogList scopePointLogList) {
        List<BigDecimal> actualResult = workDayList.stream()
                .filter(workDay ->
                        workDay.localDate().isBefore(LocalDate.now())
                        || workDay.localDate().equals(LocalDate.now()))
                .map(workDay -> scopePointLogList.resultLeftPoint(
                        workDay.localDate(), burnIncrement
                ))
                .collect(Collectors.toList());
        actualResult.add(0, scopePointLogList.pointAt(previousWorkDay.localDate()));
        return actualResult;
    }

    private List<BigDecimal> getChangedPlan(Plan plan, Resource resource, List<WorkDay> workDayList, WorkDay previousWorkDay, ScopePointLogList scopePointLogList) {
        List<BigDecimal> changedPlan = workDayList.stream()
                .map(workDay -> scopePointLogList.expectedLeftPoint(
                        workDay.localDate(), resource.storyPointIncrement(plan.period())
                ))
                .collect(Collectors.toList());
        changedPlan.add(0, scopePointLogList.pointAt(previousWorkDay.localDate()));
        return changedPlan;
    }

    private List<BigDecimal> getInitialPlan(Plan plan, Resource resource, List<WorkDay> workDayList, ScopePointLogList scopePointLogList) {
        List<BigDecimal> initialPlan = workDayList.stream()
                .map(workDay -> scopePointLogList.expectedLeftPointIgnoreChange(
                        workDay.localDate(), resource.storyPointIncrement(plan.period())
                ))
                .collect(Collectors.toList());
        initialPlan.add(0, scopePointLogList.initialPoint());
        return initialPlan;
    }

    private List<LocalDate> getPeriod(List<WorkDay> workDayList, WorkDay previousWorkDay) {
        List<LocalDate> period = workDayList.stream()
                .map(WorkDay::localDate)
                .collect(Collectors.toList());
        period.add(0, previousWorkDay.localDate());
        return period;
    }

    @Override
    public void flash() {
        this.scopeChangeLogMap = new HashMap<>();
    }
}
