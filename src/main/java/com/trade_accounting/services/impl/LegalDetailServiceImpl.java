package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.services.interfaces.LegalDetailService;
import com.trade_accounting.services.interfaces.api.LegalDetailApi;
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
public class LegalDetailServiceImpl implements LegalDetailService {

    private final LegalDetailApi legalDetailApi;

    private final String legalDetailUrl;

    private LegalDetailDto legalDetailDto;

    private final CallExecuteService<LegalDetailDto> dtoCallExecuteService;

    public LegalDetailServiceImpl(@Value("${legal_detail_url}") String legalDetailUrl, Retrofit retrofit, CallExecuteService<LegalDetailDto> dtoCallExecuteService) {
        this.legalDetailUrl = legalDetailUrl;
        legalDetailApi = retrofit.create(LegalDetailApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<LegalDetailDto> getAll() {
        Call<List<LegalDetailDto>> legalDetailListCall = legalDetailApi.getAll(legalDetailUrl);
        return dtoCallExecuteService.callExecuteBodyList(legalDetailListCall, LegalDetailDto.class);
    }

    @Override
    public LegalDetailDto getById(Long id) {
        Call<LegalDetailDto> legalDetailDtoCall = legalDetailApi.getById(legalDetailUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(legalDetailDtoCall, legalDetailDto, LegalDetailDto.class, id);
    }

    @Override
    public LegalDetailDto create(LegalDetailDto legalDetailDto) {
        Call<LegalDetailDto> legalDetailDtoCall = legalDetailApi.create(legalDetailUrl, legalDetailDto);
        try {
            this.legalDetailDto = legalDetailDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра LegalDetailDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра LegalDetailDto - {}", e);
        }
        return this.legalDetailDto;
    }


    @Override
    public void update(LegalDetailDto legalDetailDto) {
        Call<Void> legalDetailDtoCall = legalDetailApi.update(legalDetailUrl, legalDetailDto);
        dtoCallExecuteService.callExecuteBodyUpdate(legalDetailDtoCall, LegalDetailDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> legalDetailDtoCall = legalDetailApi.deleteById(legalDetailUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(legalDetailDtoCall, LegalDetailDto.class, id);
    }
}
