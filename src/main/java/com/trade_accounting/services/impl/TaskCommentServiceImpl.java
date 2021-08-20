package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaskCommentDto;
import com.trade_accounting.services.interfaces.TaskCommentService;
import com.trade_accounting.services.interfaces.api.TaskCommentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskCommentServiceImpl implements TaskCommentService {

    private final TaskCommentApi taskCommentApi;

    private final String taskCommentUrl;

    private final CallExecuteService<TaskCommentDto> dtoCallExecuteService;

    @Autowired
    public TaskCommentServiceImpl(@Value("${task_comment_url}") String taskCommentUrl, Retrofit retrofit, CallExecuteService<TaskCommentDto> dtoCallExecuteService) {
        taskCommentApi = retrofit.create(TaskCommentApi.class);
        this.taskCommentUrl = taskCommentUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TaskCommentDto> getAll() {
        Call<List<TaskCommentDto>> taskCommentDtoListCall = taskCommentApi.getAll(taskCommentUrl);
        return dtoCallExecuteService.callExecuteBodyList(taskCommentDtoListCall, TaskCommentDto.class);
    }

    @Override
    public void create(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.create(taskCommentUrl, taskCommentDto);
        dtoCallExecuteService.callExecuteBodyCreate(taskCommentDtoCall, TaskCommentDto.class);
    }

    @Override
    public void update(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.update(taskCommentUrl, taskCommentDto);
        dtoCallExecuteService.callExecuteBodyUpdate(taskCommentDtoCall, TaskCommentDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taskCommentDtoCall = taskCommentApi.deleteById(taskCommentUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(taskCommentDtoCall, TaskCommentDto.class, id);
    }
}