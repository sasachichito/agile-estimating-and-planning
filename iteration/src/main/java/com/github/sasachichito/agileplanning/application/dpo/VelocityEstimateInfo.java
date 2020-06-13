package com.github.sasachichito.agileplanning.application.dpo;

import com.github.sasachichito.agileplanning.domain.model.resource.Resource;
import com.github.sasachichito.agileplanning.domain.model.story.Story;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
@Builder
public class VelocityEstimateInfo {
    private Resource resource;
    private Story story;
    private BigDecimal storyPoint;
}
