package com.github.sasachichito.agileplanning.port.adapter.resource.plan.presentationmodel;

import com.github.sasachichito.agileplanning.domain.model.plan.cost.TotalCost;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter
public class JsonTotalCost {

    private static DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String totalCost;

    public JsonTotalCost(TotalCost totalCost) {
        this.totalCost = decimalFormat.format(totalCost.totalCost());
    }
}
