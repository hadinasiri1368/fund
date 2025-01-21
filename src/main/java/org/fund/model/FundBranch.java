package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_FUND_BRANCH")
@Entity(name = "fundBranch")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundBranch extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "IS_ACTIVE", columnDefinition = "NUMBER(1)", nullable = false)
    private Boolean isActive;
    @Column(name = "CODE", columnDefinition = "VARCHAR2(50)", nullable = false)
    private String code;
    @Column(name = "NAME", columnDefinition = "VARCHAR2(50)", nullable = false)
    private String name;
    @Column(name = "MANAGER", columnDefinition = "VARCHAR2(50)")
    private String manager;
    @Column(name = "PHONE", columnDefinition = "VARCHAR2(50)")
    private String phone;
    @Column(name = "FAX", columnDefinition = "VARCHAR2(50)")
    private String fax;
    @Column(name = "CELL_PHONE", columnDefinition = "VARCHAR2(50)")
    private String cellPhone;
    @Column(name = "POSTAL_CODE", columnDefinition = "VARCHAR2(50)")
    private String postalCode;
    @Column(name = "ADDRESS", columnDefinition = "VARCHAR2(1000)")
    private String address;
}
