package com.github.sasachichito.agileplanning.port.adapter.resource.plan.request;

import io.swagger.annotations.ApiModelProperty;

public class PlanRequestNoPeriod {
    @ApiModelProperty(value = "プランタイトル", example = "〇〇システム開発PJ", position = 1)
    public String planTitle;

    @ApiModelProperty(value = "スコープID", example = "1", position = 2)
    public int scopeId;

    @ApiModelProperty(value = "リソースID", example = "1", position = 3)
    public int resourceId;
}
