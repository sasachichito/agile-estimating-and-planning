package com.github.sasachichito.agileplanning.domain.model.scope;

import java.util.Set;

public interface ScopeRepository {
    Set<Scope> getAll();
    Scope get(ScopeId scopeId);
    void add(Scope scope);
    void put(Scope scope);
    void remove(ScopeId scopeId);
    ScopeId nextId();
    void flash();
    boolean exist(ScopeId scopeId);
}
