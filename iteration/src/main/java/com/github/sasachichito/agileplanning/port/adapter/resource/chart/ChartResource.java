package com.github.sasachichito.agileplanning.port.adapter.resource.chart;

import com.github.sasachichito.agileplanning.application.service.ChartService;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.port.adapter.resource.chart.presentationmodel.JsonBurndownLineChart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Chart", description = "チャート", tags = { "Chart" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/charts")
public class ChartResource {

    private final ChartService chartService;

    @ApiOperation(value = "バーンダウンチャート取得")
    @GetMapping("burndown/line/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public JsonBurndownLineChart burnDownChart(@PathVariable int planId) {
        return new JsonBurndownLineChart(
                this.chartService.burndownLineChart(new PlanId(planId))
        );
    }

    @ApiOperation(value = "バーンダウンチャートリスト取得")
    @GetMapping("burndown/lines/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public List<JsonBurndownLineChart> burnDownCharts(@PathVariable int planId) {
        return this.chartService.burndownLineCharts(new PlanId(planId)).burndownLineChartList().stream()
                .map(JsonBurndownLineChart::new)
                .collect(Collectors.toList());
    }
}