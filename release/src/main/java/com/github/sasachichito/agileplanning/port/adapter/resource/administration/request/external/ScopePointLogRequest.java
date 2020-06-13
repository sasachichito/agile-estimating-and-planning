package com.github.sasachichito.agileplanning.port.adapter.resource.administration.request.external;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ScopePointLogRequest {
    public int planId;
    public String dateTime;
    public BigDecimal scopePoint;
    public String changeType;
}
