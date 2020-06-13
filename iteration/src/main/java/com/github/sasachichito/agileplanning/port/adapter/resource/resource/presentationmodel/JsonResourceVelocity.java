package com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceInterest;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.velocity.VelocityEstimate;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel(value = "リソース(ベロシティ版)のJSON表現")
public class JsonResourceVelocity implements ResourceInterest, ResourceEntryListInterest {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Getter
    @ApiModelProperty(value = "リソースID", example = "1", position = 1)
    private int resourceId;

    @Getter
    @ApiModelProperty(position = 2)
    private List<JsonResourceVelocityEntry> resourceEntryList;

    private Story story;
    private BigDecimal storyPoint;

    public JsonResourceVelocity(Resource resource, Story story, BigDecimal storyPoint) {
        this.story = story;
        this.storyPoint = storyPoint;
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
                .map(resourceEntry -> new JsonResourceVelocityEntry(resourceEntry, this.story, this.storyPoint))
                .collect(Collectors.toList());
    }

    public static class JsonResourceVelocityEntry implements ResourceEntryInterest, MemberSetInterest {

        @Getter
        @ApiModelProperty(value = "リソース稼働開始日", example = "2020-01-01", position = 1)
        private String periodStart;

        @Getter
        @ApiModelProperty(value = "リソース稼働終了日", example = "2020-12-31", position = 2)
        private String periodEnd;

        @Getter
        @ApiModelProperty(position = 3)
        private JsonVelocity velocity;

        private Story story;
        private BigDecimal storyPoint;

        public JsonResourceVelocityEntry(ResourceEntry resourceEntry, Story story, BigDecimal storyPoint) {
            this.story = story;
            this.storyPoint = storyPoint;
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
            this.velocity = new JsonVelocity(
                    new MemberSet(memberSet).velocityEstimate(this.story, this.storyPoint)
            );
        }
    }

    @Getter
    public static class JsonVelocity {
        @ApiModelProperty(value = "ストーリーポイント", example = "5", position = 1)
        private BigDecimal storyPoint;

        @ApiModelProperty(value = "費用", example = "10000", position = 3)
        private BigDecimal cost;

        public JsonVelocity(VelocityEstimate velocityEstimate) {
            this.storyPoint = velocityEstimate.storyPoint().setScale(0, RoundingMode.DOWN);
            this.cost = velocityEstimate.cost();
        }
    }
}
