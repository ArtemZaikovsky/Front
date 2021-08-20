package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ProjectDto;
import com.trade_accounting.models.dto.RetailStoreDto;
import com.trade_accounting.services.interfaces.RetailStoreService;
import com.trade_accounting.services.interfaces.api.RetailStoreApi;
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
public class RetailStoreServiceImpl implements RetailStoreService {

    private final RetailStoreApi retailStoreApi;

    private final String retailStoreUrl;

    private final CallExecuteService<RetailStoreDto> dtoCallExecuteService;

    public RetailStoreServiceImpl(Retrofit retrofit, @Value("${retail_stores_url}") String retailStoreUrl, CallExecuteService<RetailStoreDto> dtoCallExecuteService) {
        retailStoreApi = retrofit.create(RetailStoreApi.class);
        this.retailStoreUrl = retailStoreUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RetailStoreDto> getAll() {

        List<RetailStoreDto> retailStoreDtoList = new ArrayList<>();
        Call<List<RetailStoreDto>> retailStoreDtoListCall = retailStoreApi.getAll(retailStoreUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailStoreDtoListCall, RetailStoreDto.class);
    }

    @Override
    public RetailStoreDto getById(Long id) {

        RetailStoreDto retailStoreDto = null;
        Call<RetailStoreDto> retailStoreDtoCall = retailStoreApi.getById(retailStoreUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(retailStoreDtoCall, retailStoreDto, RetailStoreDto.class, id);
    }

    @Override
    public void create(RetailStoreDto retailStoreDto) {
        Call<Void> retailStoreDtoCall = retailStoreApi.create(retailStoreUrl, retailStoreDto);
        dtoCallExecuteService.callExecuteBodyCreate(retailStoreDtoCall, RetailStoreDto.class);
    }

    @Override
    public void update(RetailStoreDto retailStoreDto) {
        Call<Void> retailStoreDtoCall = retailStoreApi.update(retailStoreUrl, retailStoreDto);
        dtoCallExecuteService.callExecuteBodyUpdate(retailStoreDtoCall, RetailStoreDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> retailStoreDtoCall = retailStoreApi.deleteById(retailStoreUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(retailStoreDtoCall, RetailStoreDto.class, id);
    }
}
