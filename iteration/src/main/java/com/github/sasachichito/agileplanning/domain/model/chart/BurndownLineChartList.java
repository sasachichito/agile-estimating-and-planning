package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnHoursCalculator;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnList;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeIdealHours;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Accessors(fluent = true)
@Getter
public class BurndownLineChartList {
    private List<BurndownLineChart> burndownLineChartList;

    public BurndownLineChartList(List<BurndownLineChart> burndownLineChartList) {
        if (burndownLineChartList.stream()
                .map(BurndownLineChart::planId)
                .distinct()
                .count() > 1) {
            throw new IllegalArgumentException("プランIDの異なるチャートが含まれています.");
        }
        this.burndownLineChartList = burndownLineChartList;
    }

    public void setActualResult(Plan plan, BurnList burnList, BurnHoursCalculator burnHoursCalculator) {
        // 実績をプロットする
        // チャートをループ
            // 日付リストをループ
                // 減算の元となる理想時間から、実績値を引いた残理想時間をリストにセット
            // END日付リストをループ
            // 残時間リストをチャートにセット
        // ENDチャートをループ
        this.burndownLineChartList.forEach(chart -> {
            List<BigDecimal> actualResult = new ArrayList<>();
            BurnIncrement burnIncrement = burnList.burnIncrement(
                    plan,
                    new Period(chart.period().get(0), chart.period().get(chart.period().size() -1)),
                    burnHoursCalculator);

            chart.period().forEach(date -> {
                BigDecimal left = this.getScopeIdealHoursAt(chart, date).hours()
                        .subtract(burnIncrement.hoursAt(date));
                actualResult.add(
                        (left.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : left
                );
            });
            chart.setActualResult(actualResult);
        });
    }

    private ScopeIdealHours getScopeIdealHoursAt(BurndownLineChart burndownLineChart, LocalDate localDate) {
        // その日付が、チャートのupdatedDateTimeより前（古い）の場合、その日における最新チャートの理想時間を返却
        if (localDate.isBefore(burndownLineChart.updatedDateTime().toLocalDate())) {
            return this.getLastScopeIdealHoursAt(burndownLineChart, localDate);
        }

        // その日付が、チャートのupdatedDateTimeと同じか、先の場合は、チャートの理想時間を返却
        return burndownLineChart.scopeIdealHours();
    }

    private ScopeIdealHours getLastScopeIdealHoursAt(BurndownLineChart burndownLineChart, LocalDate localDate) {
        // 指定日における最新チャートの理想時間
        return this.burndownLineChartList.stream()
                .filter(chart -> chart.updatedDateTime().toLocalDate().isEqual(localDate)
                        || chart.updatedDateTime().toLocalDate().isBefore(localDate))
                .max(Comparator.comparing(BurndownLineChart::updatedDateTime))
                .map(BurndownLineChart::scopeIdealHours)
                // 指定日までのチャートが存在しない場合は、元のチャートの理想時間を返却する
                .orElse(burndownLineChart.scopeIdealHours());
    }
}
