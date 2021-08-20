package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptanceDtoForValidation {

    private LocalDateTime dateValid;

    private ContractorDto contractorDtoValid;

    private WarehouseDto warehouseDtoValid;

    private ContractDto contractDtoValid;

    private String idValid;
}
