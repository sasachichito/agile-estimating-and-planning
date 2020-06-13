package com.github.sasachichito.agileplanning.port.adapter.service.iteration;

import com.github.sasachichito.agileplanning.domain.model.iteration.IterationPlanningService;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import com.github.sasachichito.agileplanning.port.adapter.service.iteration.request.PutStoryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IterationPlanningServiceImpl implements IterationPlanningService {

    @Value("${agile-iteration-planning.url}")
    private String baseUrl;

    private WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

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
