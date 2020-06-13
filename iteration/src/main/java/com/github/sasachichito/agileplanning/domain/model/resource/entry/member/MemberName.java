package com.github.sasachichito.agileplanning.domain.model.resource.entry.member;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class MemberName {
    @Getter
    private String name;

    public MemberName(String name) {
        this.name = name;
    }
}
