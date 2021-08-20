package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemainDto {
    private Long id;

    private String name;

    private String vendorCode;

    private Integer balance;

    private Integer irreducibleBalance;

    private Integer reserve;

    private Integer expectation;

    private Integer available;

    private List<UnitDto> unitDto;

    private Integer daysOnWarehouse;

    private Integer costPrice;

    private Integer sumOfCostPrice;

    private Integer salesCost;

    private Integer salesSum;
}
