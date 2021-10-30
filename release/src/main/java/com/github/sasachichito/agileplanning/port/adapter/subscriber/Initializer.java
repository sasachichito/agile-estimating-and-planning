package com.github.sasachichito.agileplanning.port.adapter.subscriber;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.domain.model.burn.event.subscriber.BurnRemover;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLogRepository;
import com.github.sasachichito.agileplanning.domain.model.chart.event.subscriber.ScopePointLogger;
import com.github.sasachichito.agileplanning.domain.model.iteration.IterationPlanningService;
import com.github.sasachichito.agileplanning.domain.model.iteration.StoryLinker;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber.PlanAdjuster;
import com.github.sasachichito.agileplanning.domain.model.plan.event.subscriber.PlanRemover;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber.ScopeChanger;
import com.github.sasachichito.agileplanning.domain.model.scope.event.subscriber.ScopeStoryRemover;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class Initializer {
    private final PlanRepository planRepository;
    private final StoryRepository storyRepository;
    private final ScopeRepository scopeRepository;
    private final ResourceRepository resourceRepository;
    private final BurnRepository burnRepository;
    private final ScopePointLogRepository scopePointLogRepository;
    private final IterationPlanningService iterationPlanningService;
    private final BurndownChartService burndownChartService;

    @PostConstruct
    public void init() {
        PlanAdjuster.instance().setRepositories(
                this.planRepository,
                this.scopeRepository,
                this.storyRepository,
                this.resourceRepository);

        ScopeStoryRemover.instance().setRepositories(this.scopeRepository);

        ScopeChanger.instance().setRepositories(this.scopeRepository);

        PlanRemover.instance().setRepositories(this.planRepository);

        BurnRemover.instance().setRepositories(this.burnRepository);

        StoryLinker.instance().setIterationPlanningService(this.iterationPlanningService);

        ScopePointLogger.instance().init(
                this.storyRepository,
                this.planRepository,
                this.scopeRepository,
                this.scopePointLogRepository,
                this.burndownChartService);
    }
}
