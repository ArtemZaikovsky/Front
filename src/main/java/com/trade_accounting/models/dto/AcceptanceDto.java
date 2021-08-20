package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceDto extends AcceptanceDtoForValidation {

    private Long id;

    private String incomingNumber;

    private String incomingNumberDate;

    private Long contractorId;

    private Long warehouseId;

    private Long contractId;

    private String comment;

    private Boolean isSent = null;

    private Boolean isPrint = null;

    private List<AcceptanceProductionDto> acceptanceProduction;
}
