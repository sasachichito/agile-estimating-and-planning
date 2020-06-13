package com.github.sasachichito.agileplanning.port.adapter.resource.plan;

import com.github.sasachichito.agileplanning.application.command.plan.PlanCreateCmd;
import com.github.sasachichito.agileplanning.application.command.plan.PlanUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.PlanService;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.StoryMilestoneList;
import com.github.sasachichito.agileplanning.domain.model.plan.milestone.TaskMilestoneList;
import com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel.*;
import com.github.sasachichito.agileplanning.port.adapter.resource.plan.request.PlanPutRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.plan.request.PlanRequestNoPeriod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Plan", description = "プラン", tags = { "Plan" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanResource {

    private final PlanService planService;

    @ApiOperation(value = "プラン取得")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JsonPlan> plans() {
        return this.planService.getAll().stream()
                .map(JsonPlan::new)
                .sorted(Comparator.comparing(JsonPlan::getPlanId))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "プラン取得")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public JsonPlan plan(@PathVariable int id) {
        return new JsonPlan(this.planService.getPlan(new PlanId(id)));
    }

    @ApiOperation(value = "プラン登録")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonPlan> addPlans(
            @ApiParam(value = "プラン登録データ")
            @RequestBody List<PlanRequestNoPeriod> planRequestNoPeriodList
    ) {
        return planRequestNoPeriodList.stream()
                .map(this::addPlan)
                .sorted(Comparator.comparing(JsonPlan::getPlanId))
                .collect(Collectors.toList());
    }

    private JsonPlan addPlan(PlanRequestNoPeriod planRequestNoPeriod) {
        PlanCreateCmd planCreateCmd = PlanCreateCmd.builder()
                .planTitle(planRequestNoPeriod.planTitle)
                .scopeId(planRequestNoPeriod.scopeId)
                .resourceId(planRequestNoPeriod.resourceId)
                .build();

        return new JsonPlan(this.planService.create(planCreateCmd));
    }

    @ApiOperation(value = "プラン更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonPlan putPlan(
            @PathVariable int id,
            @ApiParam(value = "プラン更新データ")
            @RequestBody PlanPutRequest planPutRequest
    ) {
        PlanUpdateCmd planUpdateCmd = PlanUpdateCmd.builder()
                .planId(id)
                .planTitle(planPutRequest.planTitle)
                .scopeId(planPutRequest.scopeId)
                .resourceId(planPutRequest.resourceId)
                .periodStart(planPutRequest.periodStart)
                .periodEnd(planPutRequest.periodEnd)
                .build();

        return new JsonPlan(this.planService.updateOrPut(planUpdateCmd));
    }

    @ApiOperation(value = "マイルストーンリスト取得")
    @GetMapping("/{id}/milestone-list/")
    @ResponseStatus(HttpStatus.OK)
    public JsonMilestone milestone(@PathVariable int id) {
        StoryMilestoneList storyMilestoneList = this.planService.generateStoryMilestoneList(new PlanId(id));
        TaskMilestoneList taskMilestoneList = this.planService.generateTaskMilestoneList(new PlanId(id));

        return new JsonMilestone(storyMilestoneList, taskMilestoneList);
    }

    @ApiOperation(value = "合計費用取得")
    @GetMapping("/{id}/total-cost/")
    @ResponseStatus(HttpStatus.OK)
    public JsonTotalCost totalCost(@PathVariable int id) {
        var totalCost = this.planService.calculateTotalCost(new PlanId(id));

        return new JsonTotalCost(totalCost);
    }

    @ApiOperation(value = "プラン削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlan(@PathVariable int id) {
        this.planService.delete(new PlanId(id));
    }

}
