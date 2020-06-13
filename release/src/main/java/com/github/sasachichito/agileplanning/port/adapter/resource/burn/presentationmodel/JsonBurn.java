package com.github.sasachichito.agileplanning.port.adapter.resource.burn.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.burn.Burn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@ApiModel(value = "バーンのJSON表現")
@Getter
public class JsonBurn {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ApiModelProperty(value = "バーンID", example = "1", position = 1)
    private int burnId;

    @ApiModelProperty(value = "ストーリーID", example = "1", position = 2)
    private int storyId;

    @ApiModelProperty(value = "バーン日時", example = "2020/01/01", position = 3)
    private String burnDate;

    public JsonBurn(Burn burn) {
        this.burnId = burn.burnId().id();
        this.storyId = burn.storyId().id();
        this.burnDate = burn.date().format(dtf);
    }
}
