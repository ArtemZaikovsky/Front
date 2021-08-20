package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.services.interfaces.ContractorGroupService;
import com.trade_accounting.services.interfaces.api.ContractorGroupApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ContractorGroupServiceImpl implements ContractorGroupService {

    private final ContractorGroupApi contractorGroupApi;

    private final String contractorGroupUrl;

    private ContractorGroupDto contractorGroupDto;

    private final CallExecuteService<ContractorGroupDto> dtoCallExecuteService;

    public ContractorGroupServiceImpl(@Value("${contractor_group_url}") String contractorGroupUrl, Retrofit retrofit, CallExecuteService<ContractorGroupDto> dtoCallExecuteService) {
        this.contractorGroupApi = retrofit.create(ContractorGroupApi.class);
        this.contractorGroupUrl = contractorGroupUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ContractorGroupDto> getAll() {
        Call<List<ContractorGroupDto>> contractorGroupDtoCallList = contractorGroupApi.getAll(contractorGroupUrl);
        return dtoCallExecuteService.callExecuteBodyList(contractorGroupDtoCallList, ContractorGroupDto.class);
    }

    @Override
    public ContractorGroupDto getById(Long id) {
        Call<ContractorGroupDto> contractorGroupDtoCall = contractorGroupApi
                .getById(contractorGroupUrl, id);
       return dtoCallExecuteService.callExecuteBodyById(contractorGroupDtoCall, contractorGroupDto, ContractorGroupDto.class, id);
    }

    @Override
    public ContractorGroupDto getByName(String name) {
        Call<ContractorGroupDto> contractorGroupDtoCallName = contractorGroupApi
                .getByName(contractorGroupUrl,name);
        try {
            contractorGroupDto = contractorGroupDtoCallName.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра ContractorGroupDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение экземпляра ContractorGroupDto с name = {}: {}", name, e);
        }
        return contractorGroupDto;
    }

    @Override
    public void create(ContractorGroupDto dto) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.create(contractorGroupUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(contractorGroupDtoCall, ContractorGroupDto.class);
    }

    @Override
    public void update(ContractorGroupDto dto) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.update(contractorGroupUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(contractorGroupDtoCall, ContractorGroupDto.class);

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contractorGroupDtoCall = contractorGroupApi.deleteById(contractorGroupUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(contractorGroupDtoCall, ContractorGroupDto.class, id);

    }
}
