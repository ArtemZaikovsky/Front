package com.trade_accounting.services.interfaces;


import com.trade_accounting.models.dto.ContractorStatusDto;

import java.util.List;

public interface ContractorStatusService {

    List<ContractorStatusDto> getAll();

    ContractorStatusDto getById(Long id);

}
