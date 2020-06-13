package com.github.sasachichito.agileplanning.port.adapter.resource.burn.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "バーンドタスク登録要求")
public class BurnedTaskRequest {
    @ApiModelProperty(value = "タスクID", example = "1", position = 1)
    public int taskId;
}
