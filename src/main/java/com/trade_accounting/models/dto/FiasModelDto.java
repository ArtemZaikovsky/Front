package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiasModelDto {
    private Integer id;
    private String aolevel;
    private String formalname;
    private String shortname;
    private String aoguid;
    private String parentguid;
}
