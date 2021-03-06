package com.github.sasachichito.agileplanning.port.adapter.resource.story;

import com.github.sasachichito.agileplanning.application.command.story.StoryUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.StoryService;
import com.github.sasachichito.agileplanning.application.command.story.StoryCreateCmd;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.port.adapter.resource.story.presentationmodel.JsonStory;
import com.github.sasachichito.agileplanning.port.adapter.resource.story.request.StoryPutRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.story.request.StoryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Story", description = "ストーリー", tags = { "Story" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryResource {

    private final StoryService storyService;

    @ApiOperation(value = "ストーリー取得")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JsonStory> stories() {
        return this.storyService.getAll().stream()
                .map(JsonStory::new)
                .sorted(Comparator.comparing(JsonStory::getStoryId))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "ストーリーポイント取得")
    @GetMapping("/{id}/story-point")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal storyPoint(@PathVariable int id) {
        return this.storyService.storyPoint(new StoryId(id));
    }

    @ApiOperation(value = "ストーリー取得")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public JsonStory story(@PathVariable int id) {
        return new JsonStory(this.storyService.get(id));
    }

    @ApiOperation(value = "ストーリー登録")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonStory> addStories(
            @ApiParam(value = "ストーリー登録データ")
            @RequestBody List<StoryRequest> storyRequestList
    ) {
        return storyRequestList.stream()
                .map(this::addStory)
                .collect(Collectors.toList());
    }

    private JsonStory addStory(@RequestBody StoryRequest storyRequest) {
        StoryCreateCmd storyCreateCmd = StoryCreateCmd.builder()
                .storyTitle(storyRequest.storyTitle())
                .storyPoint(
                        new StoryCreateCmd.StoryPoint(
                                storyRequest.storyPoint.estimate50pct,
                                storyRequest.storyPoint.estimate90pct))
                .links(storyRequest.links)
                .build();

        return new JsonStory(this.storyService.create(storyCreateCmd));
    }

    @ApiOperation(value = "ストーリー更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonStory putStory(
            @PathVariable int id,
            @ApiParam(value = "ストーリー更新データ")
            @RequestBody StoryPutRequest storyPutRequest
    ) {
        StoryUpdateCmd storyUpdateCmd = StoryUpdateCmd.builder()
                .storyId(id)
                .storyTitle(storyPutRequest.storyTitle())
                .storyPoint(
                        new StoryUpdateCmd.StoryPoint(
                                storyPutRequest.storyPoint.estimate50pct,
                                storyPutRequest.storyPoint.estimate90pct))
                .links(storyPutRequest.links)
                .build();

        Story story = this.storyService.updateOrPut(storyUpdateCmd);
        return new JsonStory(story);
    }

    @ApiOperation(value = "ストーリー削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStory(@PathVariable int id) {
        this.storyService.delete(id);
    }
}
