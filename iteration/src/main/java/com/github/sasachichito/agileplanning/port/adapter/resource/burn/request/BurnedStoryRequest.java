package com.github.sasachichito.agileplanning.port.adapter.resource.burn.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "バーンドストーリー更新要求")
public class BurnedStoryRequest {
    @ApiModelProperty(value = "ストーリーID", example = "1", position = 1)
    public int storyId;
}
