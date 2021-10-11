package com.github.sasachichito.agileplanning.port.adapter.resource.chart;

import com.github.sasachichito.agileplanning.application.service.ChartService;
import com.github.sasachichito.agileplanning.domain.model.chart.BurndownLineChart;
import com.github.sasachichito.agileplanning.domain.model.plan.PlanId;
import com.github.sasachichito.agileplanning.port.adapter.resource.chart.presentationmodel.JsonBurndownLineChart;
import com.github.sasachichito.agileplanning.port.adapter.resource.chart.request.CommentRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Chart", description = "チャート", tags = { "Chart" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/charts")
public class ChartResource {

    private final ChartService chartService;

    @ApiOperation(value = "バーンダウンチャートリスト取得")
    @GetMapping("burndown/lines/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public List<JsonBurndownLineChart> burnDownCharts(@PathVariable int planId) {
        return this.chartService.burndownLineCharts(new PlanId(planId)).burndownLineChartList().stream()
                .map(JsonBurndownLineChart::new)
                .sorted(Comparator.comparing(JsonBurndownLineChart::getVersion).reversed())
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "コメント登録")
    @PostMapping("burndown/lines")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCommnet(
            @ApiParam(value = "コメント登録データ")
            @RequestBody CommentRequest commentRequest
    ) {
        this.chartService.addCommnet(
                new PlanId(commentRequest.planId),
                commentRequest.version,
                commentRequest.comment);
    }

    public List<JsonBurndownLineChart> burnDownCharts() {
        return this.chartService.burndownLineCharts().stream()
                .map(JsonBurndownLineChart::new)
                .sorted(Comparator.comparing(JsonBurndownLineChart::getVersion).reversed())
                .collect(Collectors.toList());
    }
}