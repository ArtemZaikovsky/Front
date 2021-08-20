package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.TypeOfPriceDto;
import java.util.List;

public interface TypeOfPriceService {
    List<TypeOfPriceDto> getAll();

    TypeOfPriceDto getById(Long id);

    void create(TypeOfPriceDto typeOfPriceDto);

    void update(TypeOfPriceDto typeOfPriceDto);

    void deleteById(Long id);
}
