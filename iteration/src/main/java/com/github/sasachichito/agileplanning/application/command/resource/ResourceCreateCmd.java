package com.github.sasachichito.agileplanning.application.command.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Accessors(fluent=true)
@Getter
@Builder
public class ResourceCreateCmd {
    private List<ResourceEntry> resourceEntryList;

    @Accessors(fluent=true)
    @Getter
    public static class ResourceEntry {
        private String periodStart;
        private String periodEnd;
        private Set<Member> memberSet;

        public ResourceEntry(String periodStart, String periodEnd, Set<Member> memberSet) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.memberSet = memberSet;
        }

        @Accessors(fluent=true)
        @Getter
        public static class Member {
            private String name;
            private BigDecimal workingHoursPerDay;
            private BigDecimal unitPricePerHour;

            public Member(String name, BigDecimal workingHoursPerDay, BigDecimal unitPricePerHour) {
                this.name = name;
                this.workingHoursPerDay = workingHoursPerDay;
                this.unitPricePerHour = unitPricePerHour;
            }
        }
    }
}
