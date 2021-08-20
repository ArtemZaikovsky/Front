package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PositionDto;

import java.util.List;

public interface PositionService {

    List<PositionDto> getAll();

    PositionDto getById(Long id);

    void create(PositionDto positionDto);

    void update(PositionDto positionDto);

    void deleteById(Long id);
}
