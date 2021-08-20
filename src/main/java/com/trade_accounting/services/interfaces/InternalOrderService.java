package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InternalOrderDto;

import java.util.List;

public interface InternalOrderService {
    List<InternalOrderDto> getAll();

    InternalOrderDto getById(Long id);

    InternalOrderDto create(InternalOrderDto internalOrderDto);

    void update(InternalOrderDto internalOrderDto);

    void deleteById(Long id);
}
