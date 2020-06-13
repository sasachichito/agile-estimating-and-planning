package com.github.sasachichito.agileplanning.port.adapter.resource.scope.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.story.AdjustedStoryIdealHours;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class JsonAdjustedStoryIdealHours {
    private Map<String, String> storyIdealHoursMap;

    public JsonAdjustedStoryIdealHours(Map<Story, AdjustedStoryIdealHours> storyIdealHoursMap) {
        Map<String, String> aStoryIdealHoursMap = new LinkedHashMap<>();

        storyIdealHoursMap.forEach(((story, adjustedStoryIdealHours) ->
            aStoryIdealHoursMap.put(
                    "id:" + story.storyId().id() + " " + story.storyTitle().title(),
                    String.format("%.1f", adjustedStoryIdealHours.hours()))
        ));

        this.storyIdealHoursMap = aStoryIdealHoursMap;
    }
}
