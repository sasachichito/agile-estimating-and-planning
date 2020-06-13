package com.github.sasachichito.agileplanning.domain.model.period;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class WorkDayListGenerator {

    public static List<WorkDay> exec(LocalDate start, LocalDate end) {
        List<WorkDay> workDayList = new ArrayList<>();
        for (LocalDate date = start;
             date.isBefore(end) || date.isEqual(end);
             date = date.plusDays(1)
        ) {
            if (isHoliday(date)) continue;
            workDayList.add(new WorkDay(date));
        }
        return workDayList;
    }

    public static WorkDay previousWorkDayOf(LocalDate date) {
        LocalDate previousDay = date.minusDays(1);
        while (isHoliday(previousDay)) {
            previousDay = previousDay.minusDays(1);
        }

        return new WorkDay(previousDay);
    }

    private static boolean isHoliday(LocalDate localDate) {
        return HolidayList.match(localDate)
                || localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || isNewYearHolidays(localDate);
    }

    private static boolean isNewYearHolidays(LocalDate localDate) {
        Month month = localDate.getMonth();
        int day = localDate.getDayOfMonth();

        if (month.equals(Month.DECEMBER) && (day == 29 || day == 30 || day == 31)) {
            return true;
        }

        if (month.equals(Month.JANUARY) && (day == 1 || day == 2 || day == 3)) {
            return true;
        }
        return false;
    }
}
