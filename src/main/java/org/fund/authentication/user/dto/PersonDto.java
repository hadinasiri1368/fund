package org.fund.authentication.user.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.validator.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long id;
    @NotEmpty(fieldName = "person.isCompany")
    private Boolean isCompany;
    @NotEmpty(fieldName = "person.firstName")
    private String firstName;
    @NotEmpty(fieldName = "person.lastName")
    private String lastName;
    @NotEmpty(fieldName = "person.birthDate")
    private String birthDate;
    @NotEmpty(fieldName = "person.nationalCode")
    private String nationalCode;
    @NotEmpty(fieldName = "person.postalCode")
    private String postalCode;
    private String birthCertificationId;
    private String birthCertificationNumber;
    private String registrationNumber;
    @NotEmpty(fieldName = "person.issuingCity")
    private String issuingCity;
    private String fax;
    @NotEmpty(fieldName = "person.cellPhone")
    private String cellPhone;
    private String email;
    private String parent;
    private String phone;
    @NotEmpty(fieldName = "person.lastName")
    private String companyName;
    @NotEmpty(fieldName = "person.lastName")
    private String address;
}
