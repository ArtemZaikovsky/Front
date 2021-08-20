package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.WarehouseDto;
import com.trade_accounting.services.interfaces.WarehouseService;
import com.trade_accounting.services.interfaces.api.WarehouseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseApi warehouseApi;
    private final String warehouseUrl;
    private final CallExecuteService<WarehouseDto> dtoCallExecuteService;

    public WarehouseServiceImpl(@Value("${warehouse_url}") String warehouseUrl, Retrofit retrofit, CallExecuteService<WarehouseDto> dtoCallExecuteService) {
        this.warehouseUrl = warehouseUrl;
        warehouseApi = retrofit.create(WarehouseApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<WarehouseDto> getAll() {
        Call<List<WarehouseDto>> warehouseDtoListCall = warehouseApi.getAll(warehouseUrl);
        return dtoCallExecuteService.callExecuteBodyList(warehouseDtoListCall, WarehouseDto.class);
    }

    @Override
    public WarehouseDto getById(Long id) {
        WarehouseDto warehouseDto = new WarehouseDto();
        Call<WarehouseDto> warehouseDtoCall = warehouseApi.getById(warehouseUrl, id);
       return dtoCallExecuteService.callExecuteBodyById(warehouseDtoCall, warehouseDto, WarehouseDto.class, id);
    }

    @Override
    public void create(WarehouseDto warehouseDto) {
        Call<Void> warehouseDtoCall = warehouseApi.create(warehouseUrl, warehouseDto);
        dtoCallExecuteService.callExecuteBodyCreate(warehouseDtoCall, WarehouseDto.class);
    }

    @Override
    public void update(WarehouseDto warehouseDto) {
        Call<Void> warehouseDtoCall = warehouseApi.update(warehouseUrl, warehouseDto);
        dtoCallExecuteService.callExecuteBodyUpdate(warehouseDtoCall, WarehouseDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> warehouseDtoCall = warehouseApi.deleteById(warehouseUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(warehouseDtoCall, WarehouseDto.class, id);
    }
}
