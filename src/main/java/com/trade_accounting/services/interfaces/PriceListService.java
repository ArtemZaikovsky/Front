package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PriceListDto;

import java.util.List;

public interface PriceListService {

    List<PriceListDto> getAll();

    PriceListDto getById(Long id);

    void create(PriceListDto priceListDto);

    void update(PriceListDto priceListDto);

    void deleteById(Long id);

}
