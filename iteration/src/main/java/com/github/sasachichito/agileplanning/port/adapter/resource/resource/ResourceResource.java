package com.github.sasachichito.agileplanning.port.adapter.resource.resource;

import com.github.sasachichito.agileplanning.application.command.resource.ResourceCreateCmd;
import com.github.sasachichito.agileplanning.application.command.resource.ResourceUpdateCmd;
import com.github.sasachichito.agileplanning.application.dpo.VelocityEstimateInfo;
import com.github.sasachichito.agileplanning.application.service.ResourceService;
import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel.JsonResource;
import com.github.sasachichito.agileplanning.port.adapter.resource.resource.presentationmodel.JsonResourceVelocity;
import com.github.sasachichito.agileplanning.port.adapter.resource.resource.request.ResourceRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Resource", description = "リソース", tags = { "Resource" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceResource {

    private final ResourceService resourceService;

    @ApiOperation(value = "リソース取得")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JsonResource> resources() {
        return this.resourceService.getAll().stream()
                .map(JsonResource::new)
                .sorted(Comparator.comparing(JsonResource::getResourceId))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "リソース取得")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public JsonResource resource(@PathVariable int id) {
        return new JsonResource(this.resourceService.get(new ResourceId(id)));
    }

    @ApiOperation(value = "ベロシティ見積もり")
    @GetMapping("/{id}/velocity-estimate/{storyId}")
    @ResponseStatus(HttpStatus.OK)
    public JsonResourceVelocity velocityEstimate(@PathVariable int id, @PathVariable int storyId) {
        VelocityEstimateInfo velocityEstimateInfo = this.resourceService.estimateVelocity(
                new ResourceId(id),
                new StoryId(storyId));

        return new JsonResourceVelocity(
                velocityEstimateInfo.resource(),
                velocityEstimateInfo.story(),
                velocityEstimateInfo.storyPoint()
        );
    }

    @ApiOperation(value = "リソース登録")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonResource> addResources(
            @ApiParam(value = "リソース登録データ")
            @RequestBody List<ResourceRequest> resourceRequestList
    ) {
        return resourceRequestList.stream()
                .map(this::addResource)
                .collect(Collectors.toList());
    }

    private JsonResource addResource(@RequestBody ResourceRequest resourceRequest) {
        List<ResourceCreateCmd.ResourceEntry> resourceEntryCmdList = new ArrayList<>();

        for (var resourceEntryReq : resourceRequest.resourceEntryList()) {
            var memberCmdSet = resourceEntryReq.memberSet.stream()
                    .map(memberReq -> new ResourceCreateCmd.ResourceEntry.Member(
                            memberReq.name, memberReq.workingHoursPerDay, memberReq.unitCostPerHour))
                    .collect(Collectors.toSet());

            resourceEntryCmdList.add(new ResourceCreateCmd.ResourceEntry(
                            resourceEntryReq.periodStart,
                            resourceEntryReq.periodEnd,
                            memberCmdSet));
        }

        ResourceCreateCmd resourceCreateCmd = ResourceCreateCmd.builder()
                .resourceEntryList(resourceEntryCmdList)
                .build();

        Resource resource = this.resourceService.create(resourceCreateCmd);
        return new JsonResource(resource);
    }

    @ApiOperation(value = "リソース更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResource putResource(
            @PathVariable int id,
            @ApiParam(value = "リソース更新データ")
            @RequestBody ResourceRequest resourceRequest
    ) {
        List<ResourceUpdateCmd.ResourceEntry> resourceEntryCmdList = new ArrayList<>();

        for (var resourceEntryReq : resourceRequest.resourceEntryList()) {
            var memberCmdSet = resourceEntryReq.memberSet.stream()
                    .map(memberReq -> new ResourceUpdateCmd.ResourceEntry.Member(
                            memberReq.name, memberReq.workingHoursPerDay, memberReq.unitCostPerHour))
                    .collect(Collectors.toSet());

            resourceEntryCmdList.add(
                    new ResourceUpdateCmd.ResourceEntry(
                            resourceEntryReq.periodStart, resourceEntryReq.periodEnd, memberCmdSet));
        }

        ResourceUpdateCmd resourceUpdateCmd = ResourceUpdateCmd.builder()
                .resourceId(id)
                .resourceEntryList(resourceEntryCmdList)
                .build();

        Resource resource = this.resourceService.updateOrPut(resourceUpdateCmd);
        return new JsonResource(resource);
    }

    @ApiOperation(value = "リソース削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeResource(@PathVariable int id) {
        this.resourceService.delete(new ResourceId(id));
    }
}
