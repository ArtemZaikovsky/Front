package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarizationProductDto {

    private Long id;

    private Long productId;

    private BigDecimal actualAmount;

    private BigDecimal price;
}
