package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.services.interfaces.CompanyService;
import com.trade_accounting.services.interfaces.api.CompanyApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyApi companyApi;

    private final String companyUrl;

    private final CallExecuteService<CompanyDto> dtoCallExecuteService;

    public CompanyServiceImpl(@Value("${company_url}") String companyUrl, Retrofit retrofit, CallExecuteService<CompanyDto> dtoCallExecuteService) {
        this.companyUrl = companyUrl;
        companyApi = retrofit.create(CompanyApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<CompanyDto> getAll() {
        Call<List<CompanyDto>> companyDtoListCall = companyApi.getAll(companyUrl);
        return dtoCallExecuteService.callExecuteBodyList(companyDtoListCall, CompanyDto.class);
    }

    @Override
    public List<CompanyDto> search(Map<String, String> query) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        Call<List<CompanyDto>> companyDtoListCall = companyApi.search(companyUrl, query);

        try {
            companyDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка CompanyDto - ", e);
        }

        return companyDtoList;
    }

    @Override
    public CompanyDto getById(Long id) {
        Call<CompanyDto> companyDtoCall = companyApi.getById(companyUrl, id);
        CompanyDto companyDto = new CompanyDto();
        return dtoCallExecuteService.callExecuteBodyById(companyDtoCall, companyDto, CompanyDto.class, id);
    }

    @Override
    public CompanyDto getByEmail(String email) {
        Call<CompanyDto> companyDtoCall = companyApi.getByEmail(companyUrl, email);
        CompanyDto companyDto = null;

        try {
            companyDto = companyDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра CompanyDto по email= {}", email);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра CompanyDto по id= {} - {}",
                    email, e);
        }
        return companyDto;
    }

    @Override
    public void create(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.create(companyUrl, companyDto);
        dtoCallExecuteService.callExecuteBodyCreate(companyDtoCall, CompanyDto.class);
    }

    @Override
    public void update(CompanyDto companyDto) {
        Call<Void> companyDtoCall = companyApi.update(companyUrl, companyDto);
        dtoCallExecuteService.callExecuteBodyUpdate(companyDtoCall, CompanyDto.class);

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> companyDtoCall = companyApi.deleteById(companyUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(companyDtoCall, CompanyDto.class, id);
    }
}
