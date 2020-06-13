package com.github.sasachichito.agileplanning.port.adapter.resource.scope;

import com.github.sasachichito.agileplanning.application.command.scope.ScopeCreateCmd;
import com.github.sasachichito.agileplanning.application.command.scope.ScopeUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.ScopeService;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel.JsonAdjustedStoryPoint;
import com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel.JsonScope;
import com.github.sasachichito.agileplanning.port.adapter.resource.scope.request.ScopeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Scope", description = "スコープ", tags = { "Scope" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/scopes")
public class ScopeResource {

    private final ScopeService scopeService;

    @ApiOperation(value = "スコープ取得")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JsonScope> scopes() {
        return this.scopeService.getAll().stream()
                .map(JsonScope::new)
                .sorted(Comparator.comparing(JsonScope::getScopeId))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "ストーリーポイントリスト取得")
    @GetMapping("/{id}/stories-point")
    @ResponseStatus(HttpStatus.OK)
    public JsonAdjustedStoryPoint storyPoint(@PathVariable int id) {
        return new JsonAdjustedStoryPoint(
                this.scopeService.getAdjustedStoryPoint(new ScopeId(id))
        );
    }

    @ApiOperation(value = "スコープ取得")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public JsonScope scope(@PathVariable int id) {
        return new JsonScope(this.scopeService.get(new ScopeId(id)));
    }

    @ApiOperation(value = "スコープ登録")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonScope> addScopes(
            @ApiParam(value = "スコープ登録データ")
            @RequestBody List<ScopeRequest> scopeRequestList
    ) {
        return scopeRequestList.stream()
                .map(this::addScope)
                .collect(Collectors.toList());
    }

    private JsonScope addScope(@RequestBody ScopeRequest scopeRequest) {
        ScopeCreateCmd scopeCreateCmd = ScopeCreateCmd.builder()
                .scopeTitle(scopeRequest.scopeTitle)
                .storyIdList(scopeRequest.storyIdList)
                .build();
        Scope scope = this.scopeService.create(scopeCreateCmd);
        return new JsonScope(scope);
    }

    @ApiOperation(value = "スコープ更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonScope putScope(
            @PathVariable int id,
            @ApiParam(value = "スコープ更新データ")
            @RequestBody ScopeRequest scopeRequest
    ) {
        ScopeUpdateCmd scopeUpdateCmd = ScopeUpdateCmd.builder()
                .scopeId(id)
                .scopeTitle(scopeRequest.scopeTitle)
                .storyIdList(scopeRequest.storyIdList)
                .build();
        Scope scope = this.scopeService.updateOrPut(scopeUpdateCmd);
        return new JsonScope(scope);
    }

    @ApiOperation(value = "スコープ削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeScope(@PathVariable int id) {
        this.scopeService.delete(new ScopeId(id));
    }
}
