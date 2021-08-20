package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Pavel Andrusov
 * @version 1.0.0
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalOrderDto extends InternalOrderDtoForValidation {
    private Long id;

    private List<Long> internalOrderProductsIds;

    @NotNull
    private String date;

    @NotNull
    private Long companyId;

    private Long warehouseId;

    private Boolean isSent = null;

    private Boolean isPrint = null;

    private String comment;
}
