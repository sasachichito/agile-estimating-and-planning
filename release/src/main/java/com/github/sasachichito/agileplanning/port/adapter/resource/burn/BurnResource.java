package com.github.sasachichito.agileplanning.port.adapter.resource.burn;

import com.github.sasachichito.agileplanning.application.command.burn.BurnUpdateCmd;
import com.github.sasachichito.agileplanning.application.service.BurnService;
import com.github.sasachichito.agileplanning.domain.model.burn.Burn;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnId;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.presentationmodel.JsonBurn;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.request.BurnPutRequest;
import com.github.sasachichito.agileplanning.port.adapter.resource.burn.request.BurnRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "バーン登録")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<JsonBurn> addBurns(
            @ApiParam(value = "バーン登録データ")
            @RequestBody List<BurnRequest> burnRequestList
    ) {
        return burnRequestList.stream()
                .map(this::addBurn)
                .collect(Collectors.toList());
    }

    private JsonBurn addBurn(BurnRequest burnRequest) {
        Burn burn = this.burnService.createBurn(burnRequest.storyId);
        return new JsonBurn(burn);
    }

    @ApiOperation(value = "バーン更新")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonBurn putBurn(
            @PathVariable int id,
            @ApiParam(value = "バーン更新データ")
            @RequestBody BurnPutRequest burnPutRequest
    ) {
        BurnUpdateCmd burnUpdateCmd = BurnUpdateCmd.builder()
                .burnId(id)
                .date(burnPutRequest.burnDate)
                .storyId(burnPutRequest.storyId)
                .build();

        return new JsonBurn(this.burnService.updateOrPut(burnUpdateCmd));
    }

    @ApiOperation(value = "バーン削除")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBurn(@PathVariable int id) {
        this.burnService.delete(new BurnId(id));
    }

}
