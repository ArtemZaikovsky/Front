package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionDto {

    private Long id;

    @NotNull
    private String date;

    @NotNull
    private Long warehouseId;

    @NotNull
    private Long companyId;

    private Boolean isSent = false;

    private Boolean isPrint = false;

    private Boolean writeOffProduct = false;

    private String comment;

    private List<Long> correctionProductIds;
}
