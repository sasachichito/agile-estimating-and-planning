package com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@ApiModel(value = "プランのJSON表現")
@Getter
public class JsonPlan {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ApiModelProperty(value = "プランID", example = "1", position = 1)
    private int planId;

    @ApiModelProperty(value = "プランタイトル", example = "〇〇システム開発PJ", position = 2)
    private String planTitle;

    @ApiModelProperty(value = "スコープID", example = "1", position = 3)
    private int scopeId;

    @ApiModelProperty(value = "リソースID", example = "1", position = 4)
    private int resourceId;

    @ApiModelProperty(value = "開始日", example = "2020/01/01", position = 5)
    private String periodStart;

    @ApiModelProperty(value = "終了日", example = "2020/12/31", position = 6)
    private String periodEnd;

    public JsonPlan(Plan plan) {
        this.planId = plan.planId().id();
        this.planTitle = plan.planTitle().title();
        this.scopeId = plan.scopeId().id();
        this.resourceId = plan.resourceId().id();
        this.periodStart = plan.period().start().format(dtf);
        this.periodEnd = plan.period().end().format(dtf);
    }
}
