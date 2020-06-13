package com.github.sasachichito.agileplanning.port.adapter.persistence.inmemory;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceRepository;
import com.github.sasachichito.agileplanning.port.adapter.exception.ResourceNotFoundException;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class InMemResourceRepository implements ResourceRepository {

    private Map<ResourceId, Resource> resourceMap = new HashMap<>();
    private int lastNumbered = 0;

    @Override
    public Set<Resource> getAll() {
        return this.resourceMap.values().stream()
                .filter(resource -> !resource.isRemoved())
                .collect(Collectors.toSet());
    }

    @Override
    public Resource get(ResourceId resourceId) {
        if (this.resourceMap.containsKey(resourceId) && !this.resourceMap.get(resourceId).isRemoved()) {
            return this.resourceMap.get(resourceId);
        }
        throw new ResourceNotFoundException("ResourceId " + resourceId.id() + " is not found");
    }

    @Override
    public void add(Resource resource) {
        this.resourceMap.put(resource.resourceId(), resource);
    }

    @Override
    public void put(Resource resource) {
        if (resource.resourceId().id() > this.lastNumbered) {
            this.lastNumbered = resource.resourceId().id();
        }

        this.resourceMap.put(resource.resourceId(), resource);
    }

    @Override
    public void remove(ResourceId resourceId) {
        if (this.resourceMap.containsKey(resourceId)) {
            this.resourceMap.remove(resourceId);
            return;
        }
        throw new ResourceNotFoundException("ResourceId " + resourceId.id() + " is not found");
    }

    @Synchronized
    @Override
    public ResourceId nextId() {
        lastNumbered++;
        return new ResourceId(lastNumbered);
    }

    @Override
    public void flash() {
        this.resourceMap = new HashMap<>();
        this.lastNumbered = 0;
    }

    @Override
    public boolean exist(ResourceId resourceId) {
        return this.resourceMap.values().stream()
                .filter(resource -> !resource.isRemoved())
                .anyMatch(resource -> resource.resourceId().equals(resourceId));
    }
}
