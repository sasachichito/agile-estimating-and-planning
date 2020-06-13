package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.MemberSet;

public interface ResourceEntryInterest {
    void inform(ResourcePeriod resourcePeriod);
    void inform(MemberSet memberSet);
}
