package com.github.sasachichito.agileplanning.application.command.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Accessors(fluent=true)
@Getter
@Builder
public class ResourceUpdateCmd {
    private int resourceId;
    private List<ResourceEntry> resourceEntryList;

    @Accessors(fluent=true)
    @Getter
    public static class ResourceEntry {
        private String periodStart;
        private String periodEnd;
        private Velocity velocity;

        public ResourceEntry(String periodStart, String periodEnd, Velocity velocity) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.velocity = velocity;
        }

        @Accessors(fluent=true)
        @Getter
        public static class Velocity {
            private int storyPoint;
            private BigDecimal cost;

            public Velocity(int storyPoint, BigDecimal cost) {
                this.storyPoint = storyPoint;
                this.cost = cost;
            }
        }
    }
}
