package com.github.sasachichito.agileplanning.domain.model.chart;

import com.github.sasachichito.agileplanning.domain.model.burn.BurnIncrement;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnList;
import com.github.sasachichito.agileplanning.domain.model.burn.BurnPointCalculator;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.plan.Plan;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
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

    public void setActualResult(Plan plan, BurnList burnList, BurnPointCalculator burnPointCalculator) {
        // 実績をプロットする
        // チャートをループ
        // 日付リストをループ
        // 減算の元となる理想ポイントから、実績値を引いた残理想ポイントをリストにセット
        // END日付リストをループ
        // 残ポイントリストをチャートにセット
        // ENDチャートをループ
        this.burndownLineChartList.forEach(chart -> {
            List<BigDecimal> actualResult = new ArrayList<>();
            BurnIncrement burnIncrement = burnList.burnIncrement(
                    plan,
                    new Period(chart.period().get(0), chart.period().get(chart.period().size() -1)),
                    burnPointCalculator);

            chart.period().forEach(date -> {
                BigDecimal left = this.getScopeIdealPointAt(chart, date).point()
                        .subtract(burnIncrement.pointAt(date));
                actualResult.add(
                        (left.compareTo(BigDecimal.ZERO) < 0) ? BigDecimal.ZERO : left
                );
            });
            chart.setActualResult(actualResult);
        });
    }

    private ScopePoint getScopeIdealPointAt(BurndownLineChart burndownLineChart, LocalDate localDate) {
        // その日付が、チャートのupdatedDateTimeより前（古い）の場合、その日における最新チャートの理想ポイントを返却
        if (localDate.isBefore(burndownLineChart.updatedDateTime().toLocalDate())) {
            return this.getLastScopePointAt(burndownLineChart, localDate);
        }

        // その日付が、チャートのupdatedDateTimeと同じか、先の場合は、チャートの理想ポイントを返却
        return burndownLineChart.scopePoint();
    }

    private ScopePoint getLastScopePointAt(BurndownLineChart burndownLineChart, LocalDate localDate) {
        // 指定日における最新チャートの理想ポイント
        return this.burndownLineChartList.stream()
                .filter(chart -> chart.updatedDateTime().toLocalDate().isEqual(localDate)
                        || chart.updatedDateTime().toLocalDate().isBefore(localDate))
                .max(Comparator.comparing(BurndownLineChart::updatedDateTime))
                .map(BurndownLineChart::scopePoint)
                // 指定日までのチャートが存在しない場合は、元のチャートの理想ポイントを返却する
                .orElse(burndownLineChart.scopePoint());
    }
}
