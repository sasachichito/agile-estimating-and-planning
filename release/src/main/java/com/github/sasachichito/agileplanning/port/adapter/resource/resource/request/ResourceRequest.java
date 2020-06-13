package com.github.sasachichito.agileplanning.port.adapter.resource.resource.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Accessors(fluent=true)
@Getter
public class ResourceRequest {
    public List<ResourceEntryRequest> resourceEntryList;

    public static class ResourceEntryRequest {
        @ApiModelProperty(value = "リソース稼働開始日", example = "2020-01-01", position = 1)
        public String periodStart;

        @ApiModelProperty(value = "リソース稼働終了日", example = "2020-12-31", position = 2)
        public String periodEnd;

        @ApiModelProperty(position = 3)
        public VelocityRequest velocity;

        public static class VelocityRequest {
            @ApiModelProperty(value = "ストーリーポイント", example = "5", position = 1)
            public int storyPoint;

            @ApiModelProperty(value = "費用", example = "10000", position = 2)
            public BigDecimal cost;
        }
    }
}
