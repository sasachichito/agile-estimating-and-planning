package com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceInterest;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel(value = "リソースのJSON表現")
@Getter
public class JsonResource implements ResourceInterest, ResourceEntryListInterest {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ApiModelProperty(value = "リソースID", example = "1", position = 1)
    private int resourceId;

    @ApiModelProperty(position = 2)
    private List<JsonResourceEntry> resourceEntryList;

    public JsonResource(Resource resource) {
        resource.provide(this);
    }

    @Override
    public void inform(ResourceId resourceId) {
        this.resourceId = resourceId.id();
    }

    @Override
    public void inform(ResourceEntryList resourceEntryList) {
        resourceEntryList.provide(this);
    }

    @Override
    public void inform(List<ResourceEntry> resourceEntryList) {
        this.resourceEntryList = resourceEntryList.stream()
                .map(JsonResourceEntry::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class JsonResourceEntry implements ResourceEntryInterest, MemberSetInterest {
        @ApiModelProperty(value = "リソース稼働開始日", example = "2020-01-01", position = 1)
        private String periodStart;

        @ApiModelProperty(value = "リソース稼働終了日", example = "2020-12-31", position = 2)
        private String periodEnd;

        @ApiModelProperty(position = 3)
        private Set<JsonMember> memberSet;

        public JsonResourceEntry(ResourceEntry resourceEntry) {
            resourceEntry.provide(this);
        }

        @Override
        public void inform(ResourcePeriod resourcePeriod) {
            this.periodStart = resourcePeriod.start().format(dtf);
            this.periodEnd = resourcePeriod.end().format(dtf);
        }

        @Override
        public void inform(MemberSet memberSet) {
            memberSet.provide(this);
        }

        @Override
        public void inform(Set<Member> memberSet) {
            this.memberSet = memberSet.stream()
                    .map(JsonMember::new)
                    .collect(Collectors.toSet());
        }
    }

    @Getter
    public static class JsonMember implements MemberInterest {
        @ApiModelProperty(value = "名前", example = "スミス", position = 1)
        private String name;

        @ApiModelProperty(value = "1日あたりの稼働時間", example = "6.5", position = 2)
        private BigDecimal workingHoursPerDay;

        @ApiModelProperty(value = "1時間あたりの単価", example = "10000", position = 3)
        private BigDecimal unitCostPerHour;

        public JsonMember(Member member) {
            member.provide(this);
        }

        @Override
        public void inform(MemberName memberName) {
            this.name = memberName.name();
        }

        @Override
        public void inform(WorkingHoursPerDay workingHoursPerDay) {
            this.workingHoursPerDay = workingHoursPerDay.hours();
        }

        @Override
        public void inform(UnitCostPerHour unitCostPerHour) {
            this.unitCostPerHour = unitCostPerHour.price();
        }
    }
}
