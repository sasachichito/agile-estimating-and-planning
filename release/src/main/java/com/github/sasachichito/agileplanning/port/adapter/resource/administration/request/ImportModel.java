package com.github.sasachichito.agileplanning.port.adapter.resource.administration.request;

import com.github.sasachichito.agileplanning.port.adapter.resource.administration.request.external.ScopePointLogRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.chart.request.ChartRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ImportModel {
    @ApiModelProperty(position = 1)
    public List<StoryRequestWithId> stories;

    @ApiModelProperty(position = 2)
    public List<ScopeRequestWithId> scopes;

    @ApiModelProperty(position = 3)
    public List<ResourceRequestWithId> resources;

    @ApiModelProperty(position = 4)
    public List<PlanPutRequestWithId> plans;

    @ApiModelProperty(position = 5)
    public List<BurnPutRequestWithId> burns;

    @ApiModelProperty(position = 6)
    public List<ScopePointLogRequest> scopePointLogs;

    @ApiModelProperty(position = 7)
    public List<ChartRequest> burndownLineCharts;
}
