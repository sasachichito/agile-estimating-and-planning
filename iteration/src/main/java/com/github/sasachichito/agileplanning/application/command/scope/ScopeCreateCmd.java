package com.github.sasachichito.agileplanning.application.command.scope;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent=true)
@Getter
@Builder
public class ScopeCreateCmd {
    public String scopeTitle;
    public List<Integer> storyIdList;
}
