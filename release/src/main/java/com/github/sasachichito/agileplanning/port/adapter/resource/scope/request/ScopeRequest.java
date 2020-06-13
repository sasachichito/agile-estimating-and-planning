package com.github.sasachichito.agileplanning.port.adapter.resource.scope.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent=true)
@Getter
public class ScopeRequest {
    @ApiModelProperty(value = "スコープタイトル", example = "〇〇コスト削減", position = 1)
    public String scopeTitle;

    @ApiModelProperty(value = "ストーリーIDリスト", example = "[1,2,3]", position = 2)
    public List<Integer> storyIdList;
}
