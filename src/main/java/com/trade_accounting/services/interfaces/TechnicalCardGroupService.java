package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TechnicalCardGroupDto;

import java.util.List;

public interface TechnicalCardGroupService {

    List<TechnicalCardGroupDto> getAll();

    TechnicalCardGroupDto getById(Long id);

    void create(TechnicalCardGroupDto dto);

    void update(TechnicalCardGroupDto dto);

    void deleteById(Long id);
}
