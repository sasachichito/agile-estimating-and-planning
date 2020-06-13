package com.github.sasachichito.agileplanning.application.command.burn;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
@Getter
@Builder
public class BurnUpdateCmd {
    private int burnId;
    private String date;
    private int storyId;
}
