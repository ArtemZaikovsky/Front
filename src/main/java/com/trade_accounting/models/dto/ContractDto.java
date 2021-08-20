package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ContractDto {

    private Long id;

    private String number;

    private String contractDate;

    private LocalDate date;

    private CompanyDto companyDto;

    private BankAccountDto bankAccountDto;

    private ContractorDto contractorDto;

    private BigDecimal amount;

    private Boolean archive;

    private String comment;

    private LegalDetailDto legalDetailDto;

    public ContractDto(Long id, String number, LocalDate date, CompanyDto companyDto, BankAccountDto bankAccountDto, ContractorDto contractorDto, BigDecimal amount, Boolean archive, String comment, LegalDetailDto legalDetailDto) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.contractDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.companyDto = companyDto;
        this.bankAccountDto = bankAccountDto;
        this.contractorDto = contractorDto;
        this.amount = amount;
        this.archive = archive;
        this.comment = comment;
        this.legalDetailDto = legalDetailDto;
    }

    public void setDate(String contractDate) {
        this.date = LocalDate.parse(contractDate);
        this.contractDate = contractDate;
    }
}
