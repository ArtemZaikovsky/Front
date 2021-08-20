package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.RemainDto;

import java.util.List;

public interface RemainService {

    List<RemainDto> getAll();

    RemainDto getById(Long id);

    void create(RemainDto remainDto);

    void update(RemainDto remainDto);

    void deleteById(Long id);

}
