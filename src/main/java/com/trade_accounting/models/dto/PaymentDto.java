package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;

    private String typeOfPayment;

    private String paymentMethods;

    private String number;

    private String time;

    private CompanyDto companyDto;

    private ContractorDto contractorDto;

    private ContractDto contractDto;

    private ProjectDto projectDto;

    private BigDecimal sum;
}
