package com.trade_accounting.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal weight;

    private BigDecimal volume;

    private BigDecimal purchasePrice;

    private String description;

    private UnitDto unitDto;

    private Boolean archive = false;

    private Boolean service = false;

    private ContractorDto contractorDto;

    private List<ProductPriceDto> productPriceDtos;

    private TaxSystemDto taxSystemDto;

    private List<ImageDto> imageDtos;

    private ProductGroupDto productGroupDto;

    private AttributeOfCalculationObjectDto attributeOfCalculationObjectDto;

    private String countryOrigin;

    private BigDecimal itemNumber;

    private String saleTax;

    private BigDecimal minimumBalance;

}
