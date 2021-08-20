package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.AcceptanceProductionDto;
import com.trade_accounting.services.interfaces.AcceptanceProductionService;
import com.trade_accounting.services.interfaces.api.AcceptanceProductionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class AcceptanceServiceProductionImpl implements AcceptanceProductionService {
    private final AcceptanceProductionApi acceptanceProductionApi;
    private final String acceptanceProductUrl;
    private final CallExecuteService<AcceptanceProductionDto> callExecuteService;
    private AcceptanceProductionDto acceptanceProductionDto;

    public AcceptanceServiceProductionImpl(Retrofit retrofit, @Value("${acceptance_product_url}") String acceptanceProductUrl,
                                           CallExecuteService<AcceptanceProductionDto> callExecuteService) {
        acceptanceProductionApi = retrofit.create(AcceptanceProductionApi.class);
        this.acceptanceProductUrl = acceptanceProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<AcceptanceProductionDto> getAll() {
        return null;
    }

    @Override
    public AcceptanceProductionDto getById(Long id) {
        Call<AcceptanceProductionDto> acceptanceProductionDtoCall = acceptanceProductionApi.getById(acceptanceProductUrl, id);
        return callExecuteService.callExecuteBodyById(acceptanceProductionDtoCall, acceptanceProductionDto, AcceptanceProductionDto.class, id);
    }

    @Override
    public AcceptanceProductionDto create(AcceptanceProductionDto acceptanceProductionDto) {
        return null;
    }

    @Override
    public void update(AcceptanceProductionDto acceptanceProductionDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
