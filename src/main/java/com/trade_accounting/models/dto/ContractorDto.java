package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractorDto {
    private Long id;
    private String name;
    private String sortNumber;
    private String phone;
    private String fax;
    private String email;

    @EqualsAndHashCode.Exclude
    private AddressDto addressDto;

    private String commentToAddress;
    private String comment;
    private String discountCardNumber;

    @EqualsAndHashCode.Exclude
    private List<ContactDto> contactDto;
    @EqualsAndHashCode.Exclude
    private ContractorGroupDto contractorGroupDto;
    @EqualsAndHashCode.Exclude
    private TypeOfPriceDto typeOfPriceDto;
    @EqualsAndHashCode.Exclude
    private EmployeeDto employeeDto;
    @EqualsAndHashCode.Exclude
    private DepartmentDto departmentDto;
    @EqualsAndHashCode.Exclude
    private List<BankAccountDto> bankAccountDto;
    @EqualsAndHashCode.Exclude
    private LegalDetailDto legalDetailDto;
    @EqualsAndHashCode.Exclude
    private ContractorStatusDto contractorStatusDto;
    @EqualsAndHashCode.Exclude
    private AccessParametersDto accessParametersDto;
}
