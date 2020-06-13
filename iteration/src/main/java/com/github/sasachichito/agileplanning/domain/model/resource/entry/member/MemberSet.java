package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.velocity.VelocityEstimate;
import com.github.sasachichito.agileplanning.domain.model.story.Story;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class MemberSet {
    private Set<Member> memberSet;

    public MemberSet(Set<Member> memberSet) {
        this.memberSet = memberSet;
    }

    public WorkingHoursPerDay totalWorkingHoursPerDay() {
        return new WorkingHoursPerDay(this.memberSet.stream()
                .map(Member::workingHoursPerDay)
                .map(WorkingHoursPerDay::hours)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    public UnitCostPerDay totalUnitCostPerDay() {
        return new UnitCostPerDay(this.memberSet.stream()
                .map(Member::unitCostPerDay)
                .map(UnitCostPerDay::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public VelocityEstimate velocityEstimate(Story story, BigDecimal storyPoint) {
        BigDecimal pointPerIdealHour = storyPoint.divide(story.idealHours().hours(),3, RoundingMode.DOWN);
        return new VelocityEstimate(
                pointPerIdealHour.multiply(this.totalWorkingHoursPerDay().hours()),
                this.totalUnitCostPerDay().price()
        );
    }

    public void provide(MemberSetInterest memberSetInterest) {
        memberSetInterest.inform(this.memberSet);
    }
}
