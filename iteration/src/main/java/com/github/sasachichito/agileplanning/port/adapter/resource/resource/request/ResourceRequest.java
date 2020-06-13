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
        public Set<MemberRequest> memberSet;

        public static class MemberRequest {
            @ApiModelProperty(value = "名前", example = "スミス", position = 1)
            public String name;

            @ApiModelProperty(value = "1日あたりの稼働時間", example = "6.5", position = 2)
            public BigDecimal workingHoursPerDay;

            @ApiModelProperty(value = "1時間あたりの単価", example = "10000", position = 3)
            public BigDecimal unitCostPerHour;
        }
    }
}
