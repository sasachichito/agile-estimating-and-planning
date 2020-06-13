package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.resource.ResourceCreateCmd;
import com.github.sasachichito.agileplanning.application.command.resource.ResourceUpdateCmd;
import com.github.sasachichito.agileplanning.domain.model.resource.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.velocity.Velocity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Resource get(ResourceId resourceId) {
        return this.resourceRepository.get(resourceId);
    }

    public Set<Resource> getAll() {
        return this.resourceRepository.getAll();
    }

    public Resource create(ResourceCreateCmd resourceCreateCmd) {
        List<ResourceCreateCmd.ResourceEntry> resourceEntryCreateCmdList = resourceCreateCmd.resourceEntryList();

        List<ResourceEntry> resourceEntryList = new ArrayList<>();

        for (var resourceEntryCreate : resourceEntryCreateCmdList) {
            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryCreate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryCreate.periodEnd(), dtf)
                    ),
                    new Velocity(resourceEntryCreate.velocity().storyPoint(), resourceEntryCreate.velocity().cost())
            );
            resourceEntryList.add(resourceEntry);
        }

        Resource resource = new Resource(
                this.resourceRepository.nextId(),
                new ResourceEntryList(resourceEntryList)
        );

        this.resourceRepository.add(resource);
        return resource;
    }

    public Resource updateOrPut(ResourceUpdateCmd resourceUpdateCmd) {
        ResourceId resourceId = new ResourceId(resourceUpdateCmd.resourceId());

        if (this.resourceRepository.exist(resourceId)) {
            return this.update(
                    this.resourceRepository.get(resourceId),
                    resourceUpdateCmd);
        }
        return this.put(resourceUpdateCmd);
    }

    private Resource put(ResourceUpdateCmd resourceUpdateCmd) {
        List<ResourceUpdateCmd.ResourceEntry> resourceEntryUpdateList = resourceUpdateCmd.resourceEntryList();

        List<ResourceEntry> resourceEntryList = new ArrayList<>();

        for (var resourceEntryUpdate : resourceEntryUpdateList) {
            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryUpdate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryUpdate.periodEnd(), dtf)
                    ),
                    new Velocity(resourceEntryUpdate.velocity().storyPoint(), resourceEntryUpdate.velocity().cost())
            );
            resourceEntryList.add(resourceEntry);
        }

        Resource resource = new Resource(
                new ResourceId(resourceUpdateCmd.resourceId()),
                new ResourceEntryList(resourceEntryList)
        );

        this.resourceRepository.put(resource);
        return resource;
    }

    private Resource update(Resource resource, ResourceUpdateCmd resourceUpdateCmd) {
        List<ResourceUpdateCmd.ResourceEntry> resourceEntryUpdateList = resourceUpdateCmd.resourceEntryList();

        List<ResourceEntry> resourceEntryList = new ArrayList<>();

        for (var resourceEntryUpdate : resourceEntryUpdateList) {
            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryUpdate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryUpdate.periodEnd(), dtf)
                    ),
                    new Velocity(resourceEntryUpdate.velocity().storyPoint(), resourceEntryUpdate.velocity().cost())
            );
            resourceEntryList.add(resourceEntry);
        }

        resource.change(new ResourceEntryList(resourceEntryList));

        this.resourceRepository.put(resource);
        return resource;
    }

    public void delete(ResourceId resourceId) {
        Resource resource = this.resourceRepository.get(resourceId);
        resource.remove();
        this.resourceRepository.put(resource);
    }
}
