package com.github.sasachichito.agileplanning.port.adapter.resource.story.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.story.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@ApiModel(value = "ストーリーのJSON表現")
@Getter
public class JsonStory implements StoryInterest {
    @ApiModelProperty(value = "ストーリーID", example = "1", position = 1)
    private int storyId;

    @ApiModelProperty(value = "ストーリータイトル", example = "〇〇の更新をメール通知できる.", position = 2)
    private String storyTitle;

    @ApiModelProperty(position = 3)
    private JsonStoryPoint storyPoint;

    @ApiModelProperty(value = "ストーリー関連リンク", example = "[https://〜, https://〜]", position = 4)
    private List<String> links;

    public JsonStory(Story story) {
        story.provide(this);
    }

    @Override
    public void inform(StoryId storyId) {
        this.storyId = storyId.id();
    }

    @Override
    public void inform(StoryTitle storyTitle) {
        this.storyTitle = storyTitle.title();
    }

    @Override
    public void inform(StoryPoint storyPoint) {
        this.storyPoint = new JsonStoryPoint(storyPoint);
    }

    @Override
    public void informLinks(List<String> links) {
        this.links = links;
    }

    @Getter
    public static class JsonStoryPoint {
        @ApiModelProperty(value = "50%見積もり", example = "3", position = 1)
        private int estimate50pct;

        @ApiModelProperty(value = "90%見積もり", example = "5", position = 2)
        private int estimate90pct;

        public JsonStoryPoint(StoryPoint storyPoint) {
            this.estimate50pct = storyPoint.estimate50pct();
            this.estimate90pct = storyPoint.estimate90pct();
        }
    }
}
