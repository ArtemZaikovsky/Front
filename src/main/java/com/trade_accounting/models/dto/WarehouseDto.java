package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class WarehouseDto {
    private Long id;

    private String name;

    private String sortNumber;

    private String address;

    private String commentToAddress;

    private String comment;

}
