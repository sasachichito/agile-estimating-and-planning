package com.github.sasachichito.agileplanning.domain.model.resource.entry;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity.Velocity;

public interface ResourceEntryInterest {
    void inform(ResourcePeriod resourcePeriod);
    void inform(Velocity velocity);
}
