package com.github.sasachichito.agileplanning.domain.model.resource;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.ResourceEntryList;

public interface ResourceInterest {
    void inform(ResourceId resourceId);
    void inform(ResourceEntryList resourceEntryList);
}
