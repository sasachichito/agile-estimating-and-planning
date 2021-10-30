package com.github.sasachichito.agileplanning.application.service;

import com.github.sasachichito.agileplanning.application.command.resource.ResourceCreateCmd;
import com.github.sasachichito.agileplanning.application.command.resource.ResourceUpdateCmd;
import com.github.sasachichito.agileplanning.application.dpo.VelocityEstimateInfo;
import com.github.sasachichito.agileplanning.domain.model.resource.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.*;
import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.*;
import com.github.sasachichito.agileplanning.domain.model.release.ReleasePlanningService;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final StoryRepository storyRepository;
    private final ReleasePlanningService releasePlanningService;

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
            Set<Member> memberSet = resourceEntryCreate.memberSet().stream()
                    .map(memberCreate -> new Member(
                            new MemberName(memberCreate.name()),
                            new WorkingHoursPerDay(memberCreate.workingHoursPerDay()),
                            new UnitCostPerHour(memberCreate.unitPricePerHour()))
                    ).collect(Collectors.toSet());

            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryCreate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryCreate.periodEnd(), dtf)
                    ),
                    new MemberSet(memberSet)
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
            Set<Member> memberSet = resourceEntryUpdate.memberSet().stream()
                    .map(memberUpdate -> new Member(
                            new MemberName(memberUpdate.name()),
                            new WorkingHoursPerDay(memberUpdate.workingHoursPerDay()),
                            new UnitCostPerHour(memberUpdate.unitPricePerHour()))
                    ).collect(Collectors.toSet());

            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryUpdate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryUpdate.periodEnd(), dtf)
                    ),
                    new MemberSet(memberSet)
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
            Set<Member> memberSet = resourceEntryUpdate.memberSet().stream()
                    .map(memberUpdate -> new Member(
                            new MemberName(memberUpdate.name()),
                            new WorkingHoursPerDay(memberUpdate.workingHoursPerDay()),
                            new UnitCostPerHour(memberUpdate.unitPricePerHour()))
                    ).collect(Collectors.toSet());

            ResourceEntry resourceEntry = new ResourceEntry(
                    new ResourcePeriod(
                            LocalDate.parse(resourceEntryUpdate.periodStart(),dtf),
                            LocalDate.parse(resourceEntryUpdate.periodEnd(), dtf)
                    ),
                    new MemberSet(memberSet)
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

    public VelocityEstimateInfo estimateVelocity(ResourceId resourceId, StoryId storyId) {
        Resource resource = this.resourceRepository.get(resourceId);

        Story story = this.storyRepository.get(storyId);

        BigDecimal storyPoint = this.releasePlanningService.storyPoint(storyId);

        return VelocityEstimateInfo.builder()
                .resource(resource)
                .story(story)
                .storyPoint(storyPoint)
                .build();
    }
}
