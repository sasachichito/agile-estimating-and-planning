package com.github.sasachichito.agileplanning.port.adapter.resource.burn;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.BurnService;
import com.github.sasachichito.agileplanning.domain.model.burn.Burn;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnId;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.presentationmodel.JsonBurn;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.request.BurnedStoryRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.request.BurnedTaskPutRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.request.BurnedTaskRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Burn", description = "バーン", tags = { "Burn" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/burns")
public class BurnResource {

    private final BurnService burnService;

    @ApiOperation(value = "バーン取得")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JsonBurn> burns() {
        return this.burnService.getAll().stream()
                .map(JsonBurn::new)
                .sorted(Comparator.comparing(JsonBurn::getBurnId))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "バーン取得")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public JsonBurn burn(@PathVariable int id) {
        return new JsonBurn(this.burnService.get(new BurnId(id)));
    }

    @ApiOperation(value = "バーンドタスク登録")
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonBurn> addTaskBurns(
            @ApiParam(value = "バーンドタスク登録データ")
            @RequestBody List<BurnedTaskRequest> burnedTaskRequestList
    ) {
        return burnedTaskRequestList.stream()
                .map(this::addTaskBurn)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "バーンドストーリー登録")
    @PostMapping("/stories")
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonBurn> addStoryBurns(
            @ApiParam(value = "バーンドストーリー登録データ")
            @RequestBody List<BurnedStoryRequest> burnedStoryRequestList
    ) {
        return burnedStoryRequestList.stream()
                .map(this::addStoryBurn)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private JsonBurn addTaskBurn(BurnedTaskRequest burnedTaskRequest) {
        Burn burn = this.burnService.createTaskBurn(burnedTaskRequest.taskId);
        return new JsonBurn(burn);
    }

    private List<JsonBurn> addStoryBurn(BurnedStoryRequest burnedStoryRequest) {
        List<Burn> burnList = this.burnService.createStoryBurn(burnedStoryRequest.storyId);
        return burnList.stream()
                .map(JsonBurn::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "バーン更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonBurn putBurn(
            @PathVariable int id,
            @ApiParam(value = "バーン更新データ")
            @RequestBody BurnedTaskPutRequest burnedTaskPutRequest
    ) {
        BurnUpdateCmd burnUpdateCmd = BurnUpdateCmd.builder()
                .burnId(id)
                .date(burnedTaskPutRequest.burnDate)
                .taskId(burnedTaskPutRequest.taskId)
                .build();

        return new JsonBurn(this.burnService.updateOrPut(burnUpdateCmd));
    }

    @ApiOperation(value = "バーン削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBurn(@PathVariable int id) {
        this.burnService.deleteBurn(new BurnId(id));
    }
}
