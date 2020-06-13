package com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceInterest;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity.Velocity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public static class JsonResourceEntry implements ResourceEntryInterest {
        @ApiModelProperty(value = "リソース稼働開始日", example = "2020/01/01", position = 1)
        private String periodStart;

        @ApiModelProperty(value = "リソース稼働終了日", example = "2020/12/31", position = 2)
        private String periodEnd;

        private JsonVelocity velocity;

        public JsonResourceEntry(ResourceEntry resourceEntry) {
            resourceEntry.provide(this);
        }

        @Override
        public void inform(ResourcePeriod resourcePeriod) {
            this.periodStart = resourcePeriod.start().format(dtf);
            this.periodEnd = resourcePeriod.end().format(dtf);
        }

        @Override
        public void inform(Velocity velocity) {
            this.velocity = new JsonVelocity(velocity);
        }
    }

    @Getter
    public static class JsonVelocity {
        @ApiModelProperty(value = "ストーリーポイント", example = "5", position = 1)
        private int storyPoint;

        @ApiModelProperty(value = "費用", example = "10000", position = 3)
        private BigDecimal cost;


        public JsonVelocity(Velocity velocity) {
            this.storyPoint = velocity.storyPoint();
            this.cost = velocity.cost();
        }
    }
}
