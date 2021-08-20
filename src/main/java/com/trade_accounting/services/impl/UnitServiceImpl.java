package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.UnitDto;
import com.trade_accounting.services.interfaces.UnitService;
import com.trade_accounting.services.interfaces.api.UnitApi;
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
public class UnitServiceImpl implements UnitService {

    private final UnitApi unitApi;

    private final String unitUrl;

    private final CallExecuteService<UnitDto> dtoCallExecuteService;

    public UnitServiceImpl(@Value("${unit_url}") String unitUrl, Retrofit retrofit, CallExecuteService<UnitDto> dtoCallExecuteService) {
        this.unitUrl = unitUrl;
        unitApi = retrofit.create(UnitApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<UnitDto> getAll() {
        Call<List<UnitDto>> unitDtoListCall = unitApi.getAll(unitUrl);
        return dtoCallExecuteService.callExecuteBodyList(unitDtoListCall, UnitDto.class);
    }

    @Override
    public UnitDto getById(Long id) {
        UnitDto unitDto = new UnitDto();
        Call<UnitDto> unitDtoCall = unitApi.getById(unitUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(unitDtoCall, unitDto, UnitDto.class, id);
    }

    @Override
    public void create(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.create(unitUrl, unitDto);
        dtoCallExecuteService.callExecuteBodyCreate(unitDtoCall, UnitDto.class);
    }

    @Override
    public void update(UnitDto unitDto) {
        Call<Void> unitDtoCall = unitApi.update(unitUrl, unitDto);
        dtoCallExecuteService.callExecuteBodyUpdate(unitDtoCall, UnitDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> unitDtoCall = unitApi.deleteById(unitUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(unitDtoCall, UnitDto.class, id);
    }
}
