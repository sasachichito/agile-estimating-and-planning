package com.github.sasachichito.agileplanning.port.adapter.service;

import com.github.sasachichito.agileplanning.domain.model.resource.entry.member.velocity.ReleasePlanningService;
import com.github.sasachichito.agileplanning.domain.model.story.StoryId;
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
}
