package com.trade_accounting.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalDetailDto {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    @EqualsAndHashCode.Exclude
    private Long addressDtoId;

    private String commentToAddress;

    private String inn;

    private String kpp;

    private String okpo;

    private String ogrn;

    private String numberOfTheCertificate;

    private String dateOfTheCertificate;

    private LocalDate date;

    private Long typeOfContractorDtoId;

    public LegalDetailDto(Long id, String lastName, String firstName, String middleName,
                          Long addressDtoId, String commentToAddress, String inn,
                          String kpp, String okpo, String ogrn, String numberOfTheCertificate,
                          LocalDate date, Long typeOfContractorDtoId) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.addressDtoId = addressDtoId;
        this.commentToAddress = commentToAddress;
        this.inn = inn;
        this.kpp = kpp;
        this.okpo = okpo;
        this.ogrn = ogrn;
        this.numberOfTheCertificate = numberOfTheCertificate;
        this.date = date;
        this.dateOfTheCertificate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.typeOfContractorDtoId = typeOfContractorDtoId;
    }

@JsonIgnore
public LocalDate getDate() {
    if (dateOfTheCertificate != null) {
        return LocalDate.parse(
                dateOfTheCertificate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } else {
        return null;
    }
}

    @JsonIgnore
    public void setDate(String dateOfTheCertificate) {
        if (dateOfTheCertificate != null) {
            this.date = LocalDate.parse(dateOfTheCertificate);
            this.dateOfTheCertificate = dateOfTheCertificate;
        }
    }
}

