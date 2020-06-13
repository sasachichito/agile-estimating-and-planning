package com.github.sasachichito.agileplanning.domain.model.resource;

import java.util.Set;

public interface ResourceRepository {
    Set<Resource> getAll();
    Resource get(ResourceId resourceId);
    void add(Resource resource);
    void put(Resource resource);
    void remove(ResourceId resourceId);
    ResourceId nextId();
    void flash();
    boolean exist(ResourceId resourceId);
}
