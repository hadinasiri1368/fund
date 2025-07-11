package org.fund.baseInformation.customer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Customer;
import org.fund.model.Person;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidNationalCode;
import org.fund.validator.ValidPersianDate;

import java.util.List;

@Getter
@Setter
public class PersonDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "isCompany")
    private Boolean isCompany;
    @NotEmpty(fieldName = "firstName")
    private String firstName;
    @NotEmpty(fieldName = "lastName")
    private String lastName;
    @NotEmpty(fieldName = "birthDate")
    @ValidPersianDate(fieldName = "birthDate")
    private String birthDate;
    @NotEmpty(fieldName = "nationalCode")
    @ValidNationalCode
    private String nationalCode;
    private String postalCode;
    private String birthCertificationId;
    private String birthCertificationNumber;
    private String registrationNumber;
    private String issuingCity;
    private String fax;
    private String cellPhone;
    private String email;
    private String parent;
    private String phone;
    private String companyName;
    private String address;
    private String latinFirstName;
    private String latinLastName;
    private Boolean isIranian;
    private Long refId;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, targetType);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
