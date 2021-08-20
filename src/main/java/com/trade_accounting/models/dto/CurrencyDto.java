package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {

    private Long id;

    private String shortName;

    private String fullName;

    private String digitalCode;

    private String letterCode;

    private String sortNumber;

    public CurrencyDto(String shortName, String digitalCode, String letterCode) {
        this.shortName = shortName;
        this.digitalCode = digitalCode;
        this.letterCode = letterCode;
    }

    public CurrencyDto(String shortName, String fullName, String digitalCode, String letterCode) {
        this(shortName, digitalCode, letterCode);
        this.fullName = fullName;
    }
}
