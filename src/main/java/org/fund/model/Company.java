package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_COMPANY")
@Entity(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class Company extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(500)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "SMS_INTO", nullable = false)
    private String smsInto;
    @Column(columnDefinition = "NUMBER(8)", name = "BOURSE_ACCOUNT_NUMBER", nullable = false)
    private Long bourseAccountNumber;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "BOURSE_ACCOUNT_NAME", nullable = false)
    private Long bourseAccountName;
}
