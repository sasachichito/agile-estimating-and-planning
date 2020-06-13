package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.scope.ScopeCreateCmd;
import com.github.sasachichito.agileplanning.application.command.scope.ScopeUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.scope.*;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScopeService {

    private final ScopeRepository scopeRepository;
    private final StoryRepository storyRepository;

    public Scope create(ScopeCreateCmd scopeCreateCmd) {
        Scope scope = new Scope(
                this.scopeRepository.nextId(),
                new ScopeTitle(scopeCreateCmd.scopeTitle),
                scopeCreateCmd.storyIdList.stream().map(StoryId::new).collect(Collectors.toList()),
                new ScopeSpec(this.storyRepository)
        );
        scopeRepository.add(scope);
        return scope;
    }

    public Scope updateOrPut(ScopeUpdateCmd scopeUpdateCmd) {
        ScopeId scopeId = new ScopeId(scopeUpdateCmd.scopeId);

        if (this.scopeRepository.exist(scopeId)) {
            return this.update(
                    this.scopeRepository.get(scopeId),
                    scopeUpdateCmd);
        }
        return this.put(scopeUpdateCmd);
    }

    private Scope put(ScopeUpdateCmd scopeUpdateCmd) {
        Scope scope = new Scope(
                new ScopeId(scopeUpdateCmd.scopeId),
                new ScopeTitle(scopeUpdateCmd.scopeTitle),
                scopeUpdateCmd.storyIdList.stream().map(StoryId::new).collect(Collectors.toList()),
                new ScopeSpec(this.storyRepository)
        );
        scopeRepository.put(scope);
        return scope;
    }

    private Scope update(Scope scope, ScopeUpdateCmd scopeUpdateCmd) {
        scope.change(
                new ScopeTitle(scopeUpdateCmd.scopeTitle),
                scopeUpdateCmd.storyIdList.stream().map(StoryId::new).collect(Collectors.toList()),
                new ScopeSpec(this.storyRepository)
        );

        scopeRepository.put(scope);
        return scope;
    }

    public Scope get(ScopeId scopeId) {
        return this.scopeRepository.get(scopeId);
    }

    public Set<Scope> getAll() {
        return this.scopeRepository.getAll();
    }

    public void delete(ScopeId scopeId) {
        Scope scope = this.scopeRepository.get(scopeId);
        scope.remove();
        this.scopeRepository.put(scope);
    }

    public Map<Story, BigDecimal> getAdjustedStoryPoint(ScopeId scopeId) {
        Scope scope = this.scopeRepository.get(scopeId);

        ControlRate controlRate = scope.controlRate(
                new ControlRateCalculator(this.storyRepository),
                scope.scopePoint(new ScopePointCalculator(this.storyRepository)));

        return scope.adjustedStoryPoint(
                controlRate,
                new AdjustedStoryPointCalculator(this.storyRepository));
    }
}
