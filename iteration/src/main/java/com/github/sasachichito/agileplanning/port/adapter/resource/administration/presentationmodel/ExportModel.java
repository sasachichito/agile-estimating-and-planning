package com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel;

import com.github.sasachichito.agileplanning.port.adapter.resource.administration.presentationmodel.external.JsonScopeIdealHoursLog;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.presentationmodel.JsonBurn;
import com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel.JsonPlan;
import com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel.JsonResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel.JsonScope;
import com.github.sasachichito.agileplanning.port.adapter.resource.story.presentationmodel.JsonStory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Builder
@Accessors(fluent = true)
@Getter
public class ExportModel {
    @ApiModelProperty(position = 1)
    public List<JsonStory> stories;

    @ApiModelProperty(position = 2)
    public List<JsonScope> scopes;

    @ApiModelProperty(position = 3)
    public List<JsonResource> resources;

    @ApiModelProperty(position = 4)
    public List<JsonPlan> plans;

    @ApiModelProperty(position = 5)
    public List<JsonBurn> burns;

    @ApiModelProperty(position = 6)
    public List<JsonScopeIdealHoursLog> scopeIdealHoursLogs;
}
