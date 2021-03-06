package com.github.sasachichito.agileplanning.domain.model.burn;

import java.util.Set;

public interface BurnRepository {
    Set<Burn> getAll();
    Burn get(BurnId burnId);
    void add(Burn burn);
    void put(Burn burn);
    void remove(BurnId burnId);
    BurnId nextId();
    void flash();
    boolean exist(BurnId burnId);
}
