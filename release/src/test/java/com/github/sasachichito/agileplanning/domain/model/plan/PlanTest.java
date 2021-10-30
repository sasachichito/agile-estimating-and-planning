package com.github.sasachichito.agileplanning.domain.model.plan;

import com.github.sasachichito.agileplanning.domain.model.chart.BurndownChartService;
import com.github.sasachichito.agileplanning.domain.model.period.Period;
import com.github.sasachichito.agileplanning.domain.model.resource.ResourceId;
import com.github.sasachichito.agileplanning.domain.model.scope.Scope;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeId;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopePoint;
import com.github.sasachichito.agileplanning.domain.model.scope.ScopeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlanTest {

    @MockBean
    ScopeRepository scopeRepository;

//    @Test
//    void Planを生成できる() {
//        try {
//            Scope scope = mock(Scope.class);
//            when(scope.scopePoint(any())).thenReturn(new ScopePoint(BigDecimal.valueOf(3)));
//
//            ScopeId scopeId = new ScopeId(1);
//            when(this.scopeRepository.get(scopeId)).thenReturn(scope);
//
//            new Plan(
//                    new PlanId(1),
//                    new PlanTitle("title"),
//                    scopeId,
//                    new ResourceId(1),
//                    new Period(
//                            LocalDate.of(2020, 1,1),
//                            LocalDate.of(2020, 1,31))
//            );
//
//            verify(this.burndownChartService, times(1)).save(any());
//        } catch (Exception e) {
//            fail();
//        }
//    }

    @ParameterizedTest
    @MethodSource("planComponentProvider")
    void Planの要素にnullを設定するとIllegalArgumentExceptionを送出する(
            PlanId planId,
            PlanTitle planTitle,
            ScopeId scopeId,
            ResourceId resourceId,
            Period period
    ) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Plan(planId, planTitle, scopeId, resourceId, period);
        });
    }
    static Stream<Arguments> planComponentProvider() {
        PlanId planId = new PlanId(1);
        PlanTitle planTitle = new PlanTitle("title");
        ScopeId scopeId = new ScopeId(1);
        ResourceId resourceId = new ResourceId(1);
        Period period = new Period(
                LocalDate.of(2020, 1,1),
                LocalDate.of(2020, 1,31));

        return Stream.of(
                arguments(null, planTitle, scopeId, resourceId, period),
                arguments(planId, null, scopeId, resourceId, period),
                arguments(planId, planTitle, null, resourceId, period),
                arguments(planId, planTitle, scopeId, null, period),
                arguments(planId, planTitle, scopeId, resourceId, null),
                arguments(null, null, null, null, null)
        );
    }

}