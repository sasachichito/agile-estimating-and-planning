package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.burn.Burn;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnId;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnRepository;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemBurnRepository implements BurnRepository {

    private Map<BurnId, Burn> burnMap = new HashMap<>();
    private int lastNumbered = 0;

    @Override
    public Set<Burn> getAll() {
        return this.burnMap.values().stream()
                .filter(burn -> !burn.isRemoved())
                .collect(Collectors.toSet());
    }

    @Override
    public Burn get(BurnId burnId) {
        if (this.burnMap.containsKey(burnId) && !this.burnMap.get(burnId).isRemoved()) {
            return this.burnMap.get(burnId);
        }
        throw new ResourceNotFoundException("BurnId " + burnId.id() + " is not found");
    }

    @Override
    public void add(Burn burn) {
        this.burnMap.put(burn.burnId(), burn);
    }

    @Override
    public void put(Burn burn) {
        if (burn.burnId().id() > this.lastNumbered) {
            this.lastNumbered = burn.burnId().id();
        }

        this.burnMap.put(burn.burnId(), burn);
    }

    @Override
    public void remove(BurnId burnId) {
        if (this.burnMap.containsKey(burnId)) {
            this.burnMap.remove(burnId);
            return;
        }
        throw new ResourceNotFoundException("BurnId " + burnId.id() + " is not found");
    }

    @Synchronized
    @Override
    public BurnId nextId() {
        this.lastNumbered++;
        return new BurnId(this.lastNumbered);
    }

    @Override
    public void flash() {
        this.burnMap = new HashMap<>();
        this.lastNumbered = 0;
    }

    @Override
    public boolean exist(BurnId burnId) {
        return this.burnMap.values().stream()
                .filter(burn -> !burn.isRemoved())
                .anyMatch(burn -> burn.burnId().equals(burnId));
    }
}
