package com.github.sasachichito.agileplanning.port.adapter.resource.burn.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "バーンドタスク更新要求")
public class BurnedTaskPutRequest {
    @ApiModelProperty(value = "バーン日時", example = "2020-01-01", position = 1)
    public String burnDate;

    @ApiModelProperty(value = "タスクID", example = "1", position = 2)
    public int taskId;
}
