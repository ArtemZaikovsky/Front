package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.AddressDto;
import com.trade_accounting.models.dto.CompanyDto;

import java.util.List;
import java.util.Map;

public interface AddressService {

    AddressDto getById(Long id);

    AddressDto create(AddressDto addressDto);

    void update(AddressDto addressDto);

    void deleteById(Long id);

}
