package com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class JsonAdjustedStoryPoint {
    private Map<String, String> storyPointMap;

    public JsonAdjustedStoryPoint(Map<Story, BigDecimal> storyPointMap) {
        Map<String, String> aStoryPointMap = new LinkedHashMap<>();

        storyPointMap.forEach(((story, storyPoint) ->
                aStoryPointMap.put(
                        "id:" + story.storyId().id() + " " + story.storyTitle().title(),
                        String.format("%.1f", storyPoint))
        ));

        this.storyPointMap = aStoryPointMap;
    }
}
