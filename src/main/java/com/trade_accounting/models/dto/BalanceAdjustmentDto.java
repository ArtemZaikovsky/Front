package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceAdjustmentDto extends BalanceAdjustmentDtoForValidation {

    private Long id;

    private String date;

    private Long companyId;

    private Long contractorId;

    private String account;

    private String cashOffice;

    private String comment;

    private String dateChanged;

    private String whoChanged;
}
