package com.github.sasachichito.agileplanning.port.adapter.resource.administration;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.plan.PlanUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.resource.ResourceUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.scope.ScopeUpdateCmd;
import com.github.sasachichito.agileplanning.application.command.story.StoryUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.*;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.domain.model.chart.*;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanRepository;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.ExportModel;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.external.JsonScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.port.adapter.resource.administration.request.ImportModel;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.BurnResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.chart.ChartResource;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private final ChartResource chartResource;

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
    private final ScopeIdealHoursLogRepository scopeIdealHoursLogRepository;
    private final ChartRepository chartRepository;

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
                .scopeIdealHoursLogs(this.scopeIdealHoursLogRepository.getAll().stream()
                        .map(JsonScopeIdealHoursLog::new)
                        .collect(Collectors.toList()))
                .burndownLineCharts(this.chartResource.burnDownCharts())
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
        this.scopeIdealHoursLogRepository.flash();
        this.chartRepository.flash();

        importModel.stories.forEach(storyRequest -> {
            StoryUpdateCmd storyUpdateCmd = StoryUpdateCmd.builder()
                    .storyId(storyRequest.storyId)
                    .storyTitle(storyRequest.storyTitle)
                    .taskList(storyRequest.taskList.stream()
                            .map(taskRequest -> new StoryUpdateCmd.Task(
                                    taskRequest.taskId,
                                    taskRequest.taskName,
                                    taskRequest.estimate50Pct,
                                    taskRequest.estimate90Pct))
                            .collect(Collectors.toList()))
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
                                    resourceEntryRequest.memberSet.stream()
                                            .map(memberRequest -> new ResourceUpdateCmd.ResourceEntry.Member(
                                                    memberRequest.name,
                                                    memberRequest.workingHoursPerDay,
                                                    memberRequest.unitCostPerHour))
                                            .collect(Collectors.toSet())))
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
                    .taskId(burnPutRequest.taskId)
                    .build();
            this.burnService.updateOrPut(burnUpdateCmd);
        });

        importModel.scopeIdealHoursLogs.forEach(request -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
            ScopeIdealHoursLog scopeIdealHoursLog = new ScopeIdealHoursLog(
                    new PlanId(request.planId),
                    LocalDateTime.parse(request.dateTime, dtf),
                    new ScopeIdealHours(request.scopeIdealHours),
                    ScopeIdealHoursLog.ChangeType.valueOf(request.changeType)
            );
            this.scopeIdealHoursLogRepository.saveLog(scopeIdealHoursLog);
        });

        importModel.burndownLineCharts.forEach(request -> {
            BurndownLineChart burndownLineChart = new BurndownLineChart(
                    new PlanId(request.planId),
                    request.version,
                    LocalDateTime.parse(request.updatedDateTime, DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss")),
                    new ScopeIdealHours(request.scopeIdealHours),
                    request.period.stream()
                        .map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/d")))
                        .collect(Collectors.toList()),
                    request.changedPlan,
                    request.comment
            );
            this.chartRepository.add(burndownLineChart);
        });
    }

    @ApiOperation(value = "データクリア")
    @PostMapping("/clear")
    @ResponseStatus(HttpStatus.OK)
    public void flash() {
        this.storyRepository.flash();
        this.scopeRepository.flash();
        this.resourceRepository.flash();
        this.planRepository.flash();
        this.burnRepository.flash();
        this.scopeIdealHoursLogRepository.flash();
        this.chartRepository.flash();
    }
}
