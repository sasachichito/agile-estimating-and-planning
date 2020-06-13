package com.github.sasachichito.agileplanning.domain.model.plan.milestone;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Getter
public class MilestoneList {
    private List<Milestone> milestoneList;

    public MilestoneList(List<Milestone> milestoneList) {
        this.milestoneList = milestoneList;
    }
}
