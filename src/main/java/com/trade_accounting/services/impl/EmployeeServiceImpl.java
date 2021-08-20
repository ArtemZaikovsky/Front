package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.PageDto;
import com.trade_accounting.services.interfaces.EmployeeService;
import com.trade_accounting.services.interfaces.api.EmployeeApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeApi employeeApi;

    private final String employeeUrl;

    private final CallExecuteService<EmployeeDto> dtoCallExecuteService;

    public EmployeeServiceImpl(@Value("${employee_url}") String employeeUrl, Retrofit retrofit, CallExecuteService<EmployeeDto> dtoCallExecuteService) {
        this.employeeUrl = employeeUrl;
        employeeApi = retrofit.create(EmployeeApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<EmployeeDto> getAll() {
        Call<List<EmployeeDto>> employeeDtoListCall = employeeApi.getAll(employeeUrl);
        return dtoCallExecuteService.callExecuteBodyList(employeeDtoListCall, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> search(Map<String, String> query) {
        List<EmployeeDto> companyDtoList = new ArrayList<>();
        Call<List<EmployeeDto>> companyDtoListCall = employeeApi.search(employeeUrl, query);
        try {
            companyDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка EmployeeDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }
        return companyDtoList;
    }

    @Override
    public PageDto<EmployeeDto> getPage(Map<String, String> filterParams, Map<String, String> sortParams, int page, int count) {
        PageDto<EmployeeDto> employeeDtoList = new PageDto<>();

        Call<PageDto<EmployeeDto>> employeeDtoListCall = employeeApi.getPage(employeeUrl, sortParams, filterParams, page, count);

        try {
            employeeDtoList = employeeDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка EmployeeDto по фильтру");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка EmployeeDto - ", e);
        }
        return employeeDtoList;
    }

    @Override
    public EmployeeDto getById(Long id) {
        EmployeeDto employeeDto = new EmployeeDto();
        Call<EmployeeDto> employeeDtoCall = employeeApi.getById(employeeUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(employeeDtoCall, employeeDto,EmployeeDto.class, id);
    }

    @Override
    public void create(EmployeeDto employeeDto) {
        Call<Void> employeeDtoCall = employeeApi.create(employeeUrl, employeeDto);
        dtoCallExecuteService.callExecuteBodyCreate(employeeDtoCall, EmployeeDto.class);
    }

    @Override
    public void update(EmployeeDto employeeDto) {
        Call<Void> employeeDtoCall = employeeApi.update(employeeUrl, employeeDto);
        dtoCallExecuteService.callExecuteBodyUpdate(employeeDtoCall, EmployeeDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> employeeDtoCall = employeeApi.deleteById(employeeUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(employeeDtoCall, EmployeeDto.class, id);
    }

    @Override
    public EmployeeDto getPrincipal() {
        EmployeeDto employeeDto = new EmployeeDto();
        Call<EmployeeDto> employeeDtoCall = employeeApi.getPrincipal(employeeUrl);
        try {
            employeeDto = employeeDtoCall.execute().body();
            log.info("Успешно выполнен запрос информации о работнике");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса информации о работнике");
        }
        return employeeDto;
    }
}
