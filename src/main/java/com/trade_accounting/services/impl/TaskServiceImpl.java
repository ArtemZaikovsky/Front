package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.TaskDto;
import com.trade_accounting.services.interfaces.TaskService;
import com.trade_accounting.services.interfaces.api.TaskApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskApi taskApi;

    private final String taskUrl;

    private final CallExecuteService<TaskDto> dtoCallExecuteService;

    @Autowired
    public TaskServiceImpl(@Value("${task_url}") String taskUrl, Retrofit retrofit, CallExecuteService<TaskDto> dtoCallExecuteService) {
        taskApi = retrofit.create(TaskApi.class);
        this.taskUrl = taskUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TaskDto> getAll() {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.getAll(taskUrl);
        return dtoCallExecuteService.callExecuteBodyList(taskDtoListCall, TaskDto.class);
    }

    @Override
    public List<TaskDto> searchBy(String searchTask) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.searchBy(taskUrl, searchTask);
        try {
            taskDtoList = taskDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка по условию: {} из TaskDto", searchTask);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка по условию: {} из TaskDto: {}", searchTask, e);
        }
        return taskDtoList;
    }

    @Override
    public List<TaskDto> searchByFilter(Map<String, String> query) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Call<List<TaskDto>> taskDtoListCall = taskApi.searchByFilter(taskUrl, query);
        try {
            taskDtoList = taskDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка по условию: {} из TaskDto", query);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка по условию: {} из TaskDto: {}", query, e);
        }
        return taskDtoList;
    }

    @Override
    public TaskDto getById(Long id) {
        TaskDto taskDto = new TaskDto();
        Call<TaskDto> taskDtoCall = taskApi.getById(taskUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(taskDtoCall, taskDto, TaskDto.class, id);
    }

    @Override
    public void create(TaskDto taskDto) {
        Call<Void> taskDtoCall = taskApi.create(taskUrl, taskDto);
        dtoCallExecuteService.callExecuteBodyCreate(taskDtoCall, TaskDto.class);
    }

    @Override
    public void update(TaskDto taskDto) {
        Call<Void> taskDtoCall = taskApi.update(taskUrl, taskDto);
        dtoCallExecuteService.callExecuteBodyUpdate(taskDtoCall, TaskDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taskDtoCall = taskApi.deleteById(taskUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(taskDtoCall, TaskDto.class, id);
    }
}