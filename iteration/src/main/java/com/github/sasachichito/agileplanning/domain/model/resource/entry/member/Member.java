package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
public class Member {
    @Getter
    private MemberName memberName;
    @Getter
    private WorkingHoursPerDay workingHoursPerDay;
    private UnitCostPerHour unitCostPerHour;

    public Member(MemberName memberName, WorkingHoursPerDay workingHoursPerDay, UnitCostPerHour unitCostPerHour) {
        this.memberName = memberName;
        this.workingHoursPerDay = workingHoursPerDay;
        this.unitCostPerHour = unitCostPerHour;
    }

    UnitCostPerDay unitCostPerDay() {
        return new UnitCostPerDay(
                this.unitCostPerHour.price().multiply(
                        this.workingHoursPerDay.hours())
        );
    }

    public void provide(MemberInterest memberInterest) {
        memberInterest.inform(this.memberName);
        memberInterest.inform(this.workingHoursPerDay);
        memberInterest.inform(this.unitCostPerHour);
    }
}
