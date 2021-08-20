package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneySubCashFlowDto {

    private Long id;

    private String time;

    private BigDecimal bankcoming;

    private BigDecimal bankexpense;

    private BigDecimal bankbalance;

    private BigDecimal cashcoming;

    private BigDecimal cashexpense;

    private BigDecimal cashbalance;

    private BigDecimal allcoming;

    private BigDecimal allexpense;

    private BigDecimal allbalance;

}
