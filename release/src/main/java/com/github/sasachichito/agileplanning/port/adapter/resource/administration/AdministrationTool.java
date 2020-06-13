package com.github.sasachichito.agileplanning.port.adapter.resource.administration;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.plan.PlanUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.resource.ResourceUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.scope.ScopeUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.story.StoryUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.*;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.ScopePointLog;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.ExportModel;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.external.JsonScopePointLog;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.request.ImportModel;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.BurnResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.plan.PlanResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.resource.ResourceResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.scope.ScopeResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.story.StoryResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Api(value = "Administration Tool", description = "管理ツール",  tags = { "Administration Tool" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdministrationTool {

    private final StoryResource storyResource;
    private final ScopeResource scopeResource;
    private final ResourceResource resourceResource;
    private final PlanResource planResource;
    private final BurnResource burnResource;

    private final StoryService storyService;
    private final ScopeService scopeService;
    private final ResourceService resourceService;
    private final PlanService planService;
    private final BurnService burnService;

    private final StoryRepository storyRepository;
    private final ScopeRepository scopeRepository;
    private final ResourceRepository resourceRepository;
    private final PlanRepository planRepository;
    private final BurnRepository burnRepository;
    private final BurndownChartService burndownChartService;

    @ApiOperation(value = "データエクスポート")
    @GetMapping("/export")
    @ResponseStatus(HttpStatus.OK)
    public ExportModel export() {
        return ExportModel.builder()
                .stories(this.storyResource.stories())
                .scopes(this.scopeResource.scopes())
                .resources(this.resourceResource.resources())
                .plans(this.planResource.plans())
                .burns(this.burnResource.burns())
                .scopePointLogs(this.burndownChartService.getAll().stream()
                    .map(JsonScopePointLog::new)
                    .collect(Collectors.toList()))
                .build();
    }

    @ApiOperation(value = "データインポート")
    @PutMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    public void imports(
            @ApiParam(value = "インポートデータ")
            @RequestBody ImportModel importModel
    ) {
        this.storyRepository.flash();
        this.scopeRepository.flash();
        this.resourceRepository.flash();
        this.planRepository.flash();
        this.burnRepository.flash();

        importModel.stories.forEach(storyRequest -> {
            StoryUpdateCmd storyUpdateCmd = StoryUpdateCmd.builder()
                    .storyId(storyRequest.storyId)
                    .storyTitle(storyRequest.storyTitle)
                    .storyPoint(
                            new StoryUpdateCmd.StoryPoint(
                                    storyRequest.storyPoint.estimate50pct,
                                    storyRequest.storyPoint.estimate90pct))
                    .links(storyRequest.links)
                    .build();
            this.storyService.updateOrPut(storyUpdateCmd);
        });

        importModel.scopes.forEach(scopeRequest -> {
            ScopeUpdateCmd scopeUpdateCmd = ScopeUpdateCmd.builder()
                    .scopeId(scopeRequest.scopeId)
                    .scopeTitle(scopeRequest.scopeTitle)
                    .storyIdList(scopeRequest.storyIdList)
                    .build();
            this.scopeService.updateOrPut(scopeUpdateCmd);
        });

        importModel.resources.forEach(resourceRequest -> {
            ResourceUpdateCmd resourceUpdateCmd = ResourceUpdateCmd.builder()
                    .resourceId(resourceRequest.resourceId)
                    .resourceEntryList(resourceRequest.resourceEntryList.stream()
                            .map(resourceEntryRequest -> new ResourceUpdateCmd.ResourceEntry(
                                    resourceEntryRequest.periodStart,
                                    resourceEntryRequest.periodEnd,
                                    new ResourceUpdateCmd.ResourceEntry.Velocity(
                                            resourceEntryRequest.velocity.storyPoint,
                                            resourceEntryRequest.velocity.cost
                                    )
                            ))
                            .collect(Collectors.toList()))
                    .build();
            this.resourceService.updateOrPut(resourceUpdateCmd);
        });

        importModel.plans.forEach(planPutRequest -> {
            PlanUpdateCmd planUpdateCmd = PlanUpdateCmd.builder()
                    .planId(planPutRequest.planId)
                    .planTitle(planPutRequest.planTitle)
                    .scopeId(planPutRequest.scopeId)
                    .resourceId(planPutRequest.resourceId)
                    .periodStart(planPutRequest.periodStart)
                    .periodEnd(planPutRequest.periodEnd)
                    .build();
            this.planService.updateOrPut(planUpdateCmd);
        });

        importModel.burns.forEach(burnPutRequest -> {
            BurnUpdateCmd burnUpdateCmd = BurnUpdateCmd.builder()
                    .burnId(burnPutRequest.burnId)
                    .date(burnPutRequest.burnDate)
                    .storyId(burnPutRequest.storyId)
                    .build();
            this.burnService.updateOrPut(burnUpdateCmd);
        });

        this.burndownChartService.flash();
        importModel.scopePointLogs.forEach(request -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
            ScopePointLog scopePointLog = new ScopePointLog(
                    new PlanId(request.planId),
                    LocalDateTime.parse(request.dateTime, dtf),
                    new ScopePoint(request.scopePoint),
                    ScopePointLog.ChangeType.valueOf(request.changeType)
            );
            this.burndownChartService.save(scopePointLog);
        });
    }
}
