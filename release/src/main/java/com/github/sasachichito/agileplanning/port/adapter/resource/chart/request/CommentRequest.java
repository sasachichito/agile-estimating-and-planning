package com.github.sasachichito.agileplanning.port.adapter.resource.chart.request;

import io.swagger.annotations.ApiModelProperty;

public class CommentRequest {
    @ApiModelProperty(value = "プランID", example = "1", position = 1)
    public int planId;
    @ApiModelProperty(value = "バージョン", example = "1", position = 2)
    public int version;
    @ApiModelProperty(value = "コメント", example = "優先タスク追加", position = 3)
    public String comment;
}
