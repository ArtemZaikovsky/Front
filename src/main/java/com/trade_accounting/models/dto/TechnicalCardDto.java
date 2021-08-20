package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalCardDto {

    private Long id;

    private String name;

    private String comment;

    private String productionCost;

    private TechnicalCardGroupDto technicalCardGroupDto;

    private List<TechnicalCardProductionDto> finalProductionDto;

    private List<TechnicalCardProductionDto> materialsDto;
}
