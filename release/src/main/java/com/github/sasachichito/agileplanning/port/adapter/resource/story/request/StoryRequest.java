package com.github.sasachichito.agileplanning.port.adapter.resource.story.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent=true)
@Getter
public class StoryRequest {
    @ApiModelProperty(value = "ストーリータイトル", example = "〇〇の更新をメール通知できる.", position = 1)
    public String storyTitle;

    @ApiModelProperty(position = 2)
    public StoryPoint storyPoint;

    @ApiModelProperty(value = "ストーリー関連リンク", example = "[https://〜, https://〜]", position = 3)
    public List<String> links;

    @Accessors(fluent=true)
    @Getter
    public static class StoryPoint {
        @ApiModelProperty(value = "50%見積もり", example = "3", position = 1)
        public int estimate50pct;

        @ApiModelProperty(value = "90%見積もり", example = "5", position = 2)
        public int estimate90pct;
    }
}
