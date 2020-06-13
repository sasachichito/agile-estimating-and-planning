package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import java.util.List;

public interface ResourceEntryListInterest {
    void inform(List<ResourceEntry> resourceEntryList);
}
