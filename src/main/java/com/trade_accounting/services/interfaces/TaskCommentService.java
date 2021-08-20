package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TaskCommentDto;

import java.util.List;

public interface TaskCommentService {

    List<TaskCommentDto> getAll();

    void create(TaskCommentDto taskCommentDto);

    void update(TaskCommentDto taskCommentDto);

    void deleteById(Long id);
}
