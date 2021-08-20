package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.AcceptanceProductionDto;

import java.util.List;

public interface AcceptanceProductionService {

    List<AcceptanceProductionDto> getAll();

    AcceptanceProductionDto getById(Long id);

    AcceptanceProductionDto create(AcceptanceProductionDto acceptanceProductionDto);

    void update(AcceptanceProductionDto acceptanceProductionDto);

    void deleteById(Long id);
}
