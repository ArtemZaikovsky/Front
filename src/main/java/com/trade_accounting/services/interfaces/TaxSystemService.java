package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TaxSystemDto;

import java.util.List;

public interface TaxSystemService {

    List<TaxSystemDto> getAll();

    TaxSystemDto getById(Long id);

    void create(TaxSystemDto taxSystemDto);

    void update(TaxSystemDto taxSystemDto);

    void deleteById(Long id);

}
