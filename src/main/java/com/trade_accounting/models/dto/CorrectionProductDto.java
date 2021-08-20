package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CorrectionProductDto {
    private Long id;

    private Long productId;

    private BigDecimal amount;

    private BigDecimal price;
}
