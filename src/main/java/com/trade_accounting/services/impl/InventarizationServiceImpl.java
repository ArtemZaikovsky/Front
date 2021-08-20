package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InventarizationDto;
import com.trade_accounting.services.interfaces.InventarizationService;
import com.trade_accounting.services.interfaces.api.InventarizationApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class InventarizationServiceImpl implements InventarizationService {

    private final InventarizationApi inventarizationApi;
    private final String inventarizationUrl;
    private final CallExecuteService<InventarizationDto> callExecuteService;

    public InventarizationServiceImpl(Retrofit retrofit, @Value("${inventarization_url}") String inventarizationUrl, CallExecuteService<InventarizationDto> callExecuteService) {
        inventarizationApi = retrofit.create(InventarizationApi.class);
        this.inventarizationUrl = inventarizationUrl;
        this.callExecuteService = callExecuteService;
    }


    @Override
    public List<InventarizationDto> getAll() {
        Call<List<InventarizationDto>> invoiceDtoListCall = inventarizationApi.getAll(inventarizationUrl);
        return callExecuteService.callExecuteBodyList(invoiceDtoListCall, InventarizationDto.class);
    }

    @Override
    public InventarizationDto getById(Long id) {
        return null;
    }

    @Override
    public InventarizationDto create(InventarizationDto inventarizationDto) {
        return null;
    }

    @Override
    public void update(InventarizationDto inventarizationDto) {

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> inventarizationDtoCall = inventarizationApi.deleteById(inventarizationUrl, id);
        callExecuteService.callExecuteBodyDelete(inventarizationDtoCall, InventarizationDto.class, id);
    }
}