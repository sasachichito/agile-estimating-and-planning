package com.github.sasachichito.agileplanning.port.adapter.resource.chart.request;

import io.swagger.annotations.ApiModelProperty;

public class CommentRequest {
    @ApiModelProperty(value = "コメント", example = "優先タスク追加", position = 1)
    public String comment;
}
