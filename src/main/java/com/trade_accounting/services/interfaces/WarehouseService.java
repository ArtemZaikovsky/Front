package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.WarehouseDto;

import java.util.List;

public interface WarehouseService {

    List<WarehouseDto> getAll();

    WarehouseDto getById(Long id);

    void create(WarehouseDto warehouseDto);

    void update(WarehouseDto warehouseDto);

    void deleteById(Long id);
}
