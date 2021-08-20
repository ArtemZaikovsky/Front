package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.DepartmentDto;
import com.trade_accounting.services.interfaces.DepartmentService;
import com.trade_accounting.services.interfaces.api.DepartmentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentApi departmentApi;

    private final String departmentUrl;

    private final CallExecuteService<DepartmentDto> dtoCallExecuteService;

    public DepartmentServiceImpl(@Value("${department_url}") String departmentUrl, Retrofit retrofit, CallExecuteService<DepartmentDto> dtoCallExecuteService) {
        this.departmentUrl = departmentUrl;
        departmentApi = retrofit.create(DepartmentApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<DepartmentDto> getAll() {
        Call<List<DepartmentDto>> departmentDtoListCall = departmentApi.getAll(departmentUrl);
        return dtoCallExecuteService.callExecuteBodyList(departmentDtoListCall, DepartmentDto.class);
    }

    @Override
    public DepartmentDto getById(Long id) {

        DepartmentDto departmentDto = null;
        Call<DepartmentDto> departmentDtoCall = departmentApi.getById(departmentUrl, id);

        try {
            departmentDto = departmentDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра DepartmentDto по id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра DepartmentDto по id= {} - {}",
                    id, e);
        }

        return departmentDto;
    }

    @Override
    public void create(DepartmentDto departmentDto) {

        Call<Void> departmentDtoCall = departmentApi.create(departmentUrl, departmentDto);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра DepartmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра DepartmentDto - {}",
                    e);
        }
    }

    @Override
    public void update(DepartmentDto departmentDto) {

        Call<Void> departmentDtoCall = departmentApi.update(departmentUrl, departmentDto);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра DepartmentDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра DepartmentDto - {}",
                    e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> departmentDtoCall = departmentApi.deleteById(departmentUrl, id);

        try {
            departmentDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра DepartmentDto с id= {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра DepartmentDto с id= {} - {}",
                    id, e);
        }
    }
}
