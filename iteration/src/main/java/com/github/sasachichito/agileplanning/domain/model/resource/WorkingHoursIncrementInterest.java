package com.github.sasachichito.agileplanning.domain.model.resource;

import java.time.LocalDate;
import java.util.Map;

public interface WorkingHoursIncrementInterest {
    void inform(Map<LocalDate, WorkingHoursIncrement.WorkingHours> workingHoursMap);
}
