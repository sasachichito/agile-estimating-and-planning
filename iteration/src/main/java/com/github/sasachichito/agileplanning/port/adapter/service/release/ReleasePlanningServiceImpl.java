package com.github.sasachichito.agileplanning.port.adapter.service.release;

import com.github.sasachichito.agileplanning.domain.model.release.ReleasePlanningService;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
import com.github.sasachichito.agileplanning.port.adapter.service.release.request.PutStoryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;


@Component
public class ReleasePlanningServiceImpl implements ReleasePlanningService {

    @Value("${agile-release-planning.url}")
    private String baseUrl;

    private WebClient webClient = WebClient.create();

    @Override
    public BigDecimal storyPoint(StoryId storyId) {
        BigDecimal storyPoint = this.webClient.get()
                .uri(this.baseUrl + "/stories/" + storyId.id() + "/story-point")
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .block();

        if (storyPoint == null) {
            throw new IllegalArgumentException("StoryId: " + storyId.id() + " StoryPointの取得失敗.");
        }

        return storyPoint;
    }

    @Override
    public void createStory(Story story) {
        PutStoryRequest putStoryRequest = new PutStoryRequest(story);

        this.webClient.put()
                .uri(this.baseUrl + "/stories/" + story.storyId().id())
                .bodyValue(putStoryRequest.json())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void updateStory(Story story) {
        PutStoryRequest putStoryRequest = new PutStoryRequest(story);

        this.webClient.put()
                .uri(this.baseUrl + "/stories/" + story.storyId().id())
                .bodyValue(putStoryRequest.json())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void deleteStory(Story story) {
        this.webClient.delete()
                .uri(this.baseUrl + "/stories/" + story.storyId().id())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
