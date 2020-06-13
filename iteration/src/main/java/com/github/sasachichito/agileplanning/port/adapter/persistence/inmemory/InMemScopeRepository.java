package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemScopeRepository implements ScopeRepository {

    private Map<ScopeId, Scope> scopeMap = new HashMap<>();
    private int lastNumbered = 0;

    @Override
    public Set<Scope> getAll() {
        return this.scopeMap.values().stream()
                .filter(scope -> !scope.isRemoved())
                .collect(Collectors.toSet());
    }

    @Override
    public Scope get(ScopeId scopeId) {
        if (this.scopeMap.containsKey(scopeId) && !this.scopeMap.get(scopeId).isRemoved()) {
            return this.scopeMap.get(scopeId);
        }
        throw new ResourceNotFoundException("ScopeId " + scopeId.id() + " is not found");
    }

    @Override
    public void add(Scope scope) {
        this.scopeMap.put(scope.scopeId(), scope);
    }

    @Override
    public void put(Scope scope) {
        if (scope.scopeId().id() > this.lastNumbered) {
            this.lastNumbered = scope.scopeId().id();
        }

        this.scopeMap.put(scope.scopeId(), scope);
    }

    @Override
    public void remove(ScopeId scopeId) {
        if (this.scopeMap.containsKey(scopeId)) {
            this.scopeMap.remove(scopeId);
            return;
        }
        throw new ResourceNotFoundException("ScopeId " + scopeId.id() + " is not found");
    }

    @Synchronized
    @Override
    public ScopeId nextId() {
        lastNumbered++;
        return new ScopeId(lastNumbered);
    }

    @Override
    public void flash() {
        this.scopeMap = new HashMap<>();
        this.lastNumbered = 0;
    }

    @Override
    public boolean exist(ScopeId scopeId) {
        return this.scopeMap.values().stream()
                .filter(scope -> !scope.isRemoved())
                .anyMatch(scope -> scope.scopeId().equals(scopeId));
    }
}
