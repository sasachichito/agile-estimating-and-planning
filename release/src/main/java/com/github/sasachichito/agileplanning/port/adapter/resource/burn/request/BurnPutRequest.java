package com.github.sasachichito.agileplanning.port.adapter.resource.burn.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "バーン更新要求")
public class BurnPutRequest {
    @ApiModelProperty(value = "バーン日時", example = "2020-01-01", position = 1)
    public String burnDate;

    @ApiModelProperty(value = "ストーリーID", example = "1", position = 2)
    public int storyId;
}
