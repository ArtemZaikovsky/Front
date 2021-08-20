package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ContractDto;
import com.trade_accounting.services.interfaces.ContractService;
import com.trade_accounting.services.interfaces.api.ContractApi;
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

@Service
@Slf4j
public class ContractServiceImpl implements ContractService  {

    private final ContractApi contractApi;

    private final String contractUrl;

    private final CallExecuteService<ContractDto> dtoCallExecuteService;

    public ContractServiceImpl(@Value("${contract_url}") String contractUrl, Retrofit retrofit, CallExecuteService<ContractDto> dtoCallExecuteService) {
        contractApi = retrofit.create(ContractApi.class);
        this.contractUrl = contractUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ContractDto> getAll() {
        Call<List<ContractDto>> contractDtoListCall = contractApi.getAll(contractUrl);
        return dtoCallExecuteService.callExecuteBodyList(contractDtoListCall, ContractDto.class);
    }

    @Override
    public List<ContractDto> searchByTerm(String searchContr) {
        List<ContractDto> contractDtoList = new ArrayList<>();
        Call<List<ContractDto>> contractDtoListCall = contractApi.getAll(contractUrl, searchContr);
        try {
            contractDtoList = contractDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка ContractDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ContractDto: {IOException}", e);
        }
        return contractDtoList;
    }

    @Override
    public ContractDto getById(Long id) {
        ContractDto contractDto = new ContractDto();
        Call<ContractDto> contractDtoCall = contractApi.getById(contractUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(contractDtoCall, contractDto, ContractDto.class, id);
    }

    @Override
    public List<ContractDto> search(Map<String, String> query) {
        List<ContractDto> contractDtoList = new ArrayList<>();
        Call<List<ContractDto>> contractDtoListCall = contractApi.search(contractUrl, query);

        try {
            contractDtoList = contractDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка договоров");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ContractDto - ", e);
        }
        return contractDtoList;
    }

    @Override
    public void create(ContractDto contractDto) {
        Call<Void> contractDtoCall = contractApi.create(contractUrl, contractDto);
        dtoCallExecuteService.callExecuteBodyCreate(contractDtoCall, ContractDto.class);
    }

    @Override
    public void update(ContractDto contractDto) {
        Call<Void> contractDtoCall = contractApi.update(contractUrl, contractDto);
        dtoCallExecuteService.callExecuteBodyUpdate(contractDtoCall, ContractDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contractDtoCall = contractApi.deleteById(contractUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(contractDtoCall, ContractDto.class, id);
    }
}
