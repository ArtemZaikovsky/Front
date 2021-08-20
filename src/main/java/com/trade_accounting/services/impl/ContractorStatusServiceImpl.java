package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractorStatusDto;
import com.trade_accounting.services.interfaces.ContractorStatusService;
import com.trade_accounting.services.interfaces.api.ContractorStatusApi;
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
public class ContractorStatusServiceImpl implements ContractorStatusService {

    private final ContractorStatusApi contractorStatusApi;

    private final String statusUrl;

    private List<ContractorStatusDto> statusDtoList = new ArrayList<>();

    private ContractorStatusDto statusDto = new ContractorStatusDto();

    private final CallExecuteService<ContractorStatusDto> dtoCallExecuteService;

    public ContractorStatusServiceImpl(@Value("${contractor_status_url}") String statusUrl, Retrofit retrofit, CallExecuteService<ContractorStatusDto> dtoCallExecuteService) {
        this.contractorStatusApi = retrofit.create(ContractorStatusApi.class);
        this.statusUrl = statusUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }


    @Override
    public List<ContractorStatusDto> getAll() {
        Call<List<ContractorStatusDto>> statusGetAllCall = contractorStatusApi.getAll(statusUrl);
        return dtoCallExecuteService.callExecuteBodyList(statusGetAllCall, ContractorStatusDto.class);
    }

    @Override
    public ContractorStatusDto getById(Long id) {
        Call<ContractorStatusDto> statusGetCall = contractorStatusApi.getById(statusUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(statusGetCall, statusDto, ContractorStatusDto.class, id);
    }

}
