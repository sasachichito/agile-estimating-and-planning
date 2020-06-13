package com.github.sasachichito.agileplanning.port.adapter.resource.plan.request;

import io.swagger.annotations.ApiModelProperty;

public class PlanPutRequest {
    @ApiModelProperty(value = "プランタイトル", example = "〇〇システム開発PJ", position = 1)
    public String planTitle;

    @ApiModelProperty(value = "スコープID", example = "1", position = 2)
    public int scopeId;

    @ApiModelProperty(value = "リソースID", example = "1", position = 3)
    public int resourceId;

    @ApiModelProperty(value = "開始日", example = "2020-01-01", position = 4)
    public String periodStart;

    @ApiModelProperty(value = "終了日", example = "2020-12-31", position = 5)
    public String periodEnd;
}
