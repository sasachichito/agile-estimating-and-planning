package com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeInterest;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeTitle;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "スコープのJSON表現")
@Getter
public class JsonScope implements ScopeInterest {
    @ApiModelProperty(value = "スコープID", example = "1", position = 1)
    private int scopeId;

    @ApiModelProperty(value = "スコープタイトル", example = "〇〇コスト削減", position = 2)
    private String scopeTitle;

    @ApiModelProperty(value = "ストーリーIDリスト", example = "[1,2,3]", position = 3)
    private List<Integer> storyIdList;

    public JsonScope(Scope scope) {
        scope.provide(this);
    }

    @Override
    public void inform(ScopeId scopeId) {
        this.scopeId = scopeId.id();
    }

    @Override
    public void inform(ScopeTitle scopeTitle) {
        this.scopeTitle = scopeTitle.title();
    }

    @Override
    public void inform(List<StoryId> storyIdList) {
        this.storyIdList = storyIdList.stream()
                .map(StoryId::id)
                .collect(Collectors.toList());
    }
}
