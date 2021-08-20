package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ContractDto;

import java.util.List;
import java.util.Map;

public interface ContractService  {
    List<ContractDto> getAll();

    List<ContractDto> searchByTerm (String searchContr);

    ContractDto getById(Long id);

    List<ContractDto> search(Map<String, String> query);

    void create(ContractDto contractDto);

    void update(ContractDto contractDto);

    void deleteById(Long id);


}
