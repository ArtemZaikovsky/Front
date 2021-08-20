package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InventarizationDto;

import java.util.List;

public interface InventarizationService {

    List<InventarizationDto> getAll();

    InventarizationDto getById(Long id);

    InventarizationDto create(InventarizationDto dto);

    void update(InventarizationDto dto);

    void deleteById(Long id);
}
