package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

public interface MemberInterest {
    void inform(MemberName memberName);
    void inform(WorkingHoursPerDay workingHoursPerDay);
    void inform(UnitCostPerHour unitCostPerHour);
}
