package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.domain.model.burn.*;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChartService {
    private final BurndownChartService burndownChartService;
    private final ScopeRepository scopeRepository;
    private final StoryRepository storyRepository;
    private final BurnRepository burnRepository;
    private final PlanRepository planRepository;
    private final ResourceRepository resourceRepository;

    public BurndownLineChart burndownLineChart(PlanId planId) {
        Plan plan = this.planRepository.get(planId);
        Resource resource = this.resourceRepository.get(plan.resourceId());

        List<Burn> aBurnList = this.burnRepository.getAll().stream()
                .filter(burn -> burn.isRelated(
                        plan.scopeId(),
                        new BurnRelationChecker(this.scopeRepository, this.storyRepository)))
                .collect(Collectors.toList());

        BurnList burnList = new BurnList(aBurnList);

        return this.burndownChartService.getLineChart(
                plan,
                resource,
                burnList.burnIncrement(plan.period(), new BurnHoursCalculator(this.scopeRepository, this.storyRepository))
        );
    }

    public List<BurndownLineChart> burndownLineCharts(PlanId planId) {
        return this.burndownChartService.getLineCharts(planId);
    }
}
