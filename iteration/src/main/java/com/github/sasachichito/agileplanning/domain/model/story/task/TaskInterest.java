package com.github.sasachichito.agileplanning.domain.model.story.task;

import java.math.BigDecimal;

public interface TaskInterest {
    void inform(TaskId taskId);
    void inform(TaskName taskName);
    void inform50(BigDecimal estimate50Pct);
    void inform90(BigDecimal estimate90Pct);
//    void inform(Task.Status status);
}
