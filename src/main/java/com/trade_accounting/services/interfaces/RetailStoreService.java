package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RetailStoreDto;

import java.util.List;

public interface RetailStoreService {

    List<RetailStoreDto> getAll();

    RetailStoreDto getById(Long id);

    void create(RetailStoreDto retailStoreDto);

    void update(RetailStoreDto retailStoreDto);

    void deleteById(Long id);

}
