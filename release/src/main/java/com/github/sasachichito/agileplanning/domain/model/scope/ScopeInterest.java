package com.github.sasachichito.agileplanning.domain.model.scope;

import com.github.sasachichito.agileplanning.domain.model.story.StoryId;

import java.util.List;

public interface ScopeInterest {
    void inform(ScopeId scopeId);
    void inform(ScopeTitle scopeTitle);
    void inform(List<StoryId> storyIdList);
}
