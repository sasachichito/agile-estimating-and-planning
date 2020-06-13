package com.github.sasachichito.agileplanning.domain.model.story.task;

import java.util.List;

public interface TaskListInterest {
    void inform(List<Task> taskList);
}
