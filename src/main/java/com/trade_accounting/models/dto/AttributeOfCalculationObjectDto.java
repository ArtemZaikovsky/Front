package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeOfCalculationObjectDto {

    private Long id;

    private String name;

    private String sortNumber;

    private Boolean isService;
}
