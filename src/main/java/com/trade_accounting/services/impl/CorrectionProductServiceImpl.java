package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.CorrectionProductDto;
import com.trade_accounting.services.interfaces.CorrectionProductService;
import com.trade_accounting.services.interfaces.api.CorrectionProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class CorrectionProductServiceImpl implements CorrectionProductService {

    private final CorrectionProductApi correctionProductApi;
    private final String correctionProductUrl;
    private final CallExecuteService<CorrectionProductDto> callExecuteService;
    private CorrectionProductDto correctionProductDto;

    public CorrectionProductServiceImpl(Retrofit retrofit, @Value("${correction_product_url}") String correctionProductUrl, CallExecuteService<CorrectionProductDto> callExecuteService) {
        correctionProductApi = retrofit.create(CorrectionProductApi.class);
        this.correctionProductUrl = correctionProductUrl;
        this.callExecuteService = callExecuteService;
    }

    @Override
    public List<CorrectionProductDto> getAll() {
        return null;
    }

    @Override
    public CorrectionProductDto getById(Long id) {
        Call<CorrectionProductDto> correctionProductDtoCall = correctionProductApi.getById(correctionProductUrl, id);
        return callExecuteService.callExecuteBodyById(correctionProductDtoCall, correctionProductDto, CorrectionProductDto.class, id);
    }

    @Override
    public CorrectionProductDto create(CorrectionProductDto correctionProductDto) {
        return null;
    }

    @Override
    public void update(CorrectionProductDto correctionProductDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
