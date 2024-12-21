package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_VERIFICATION_CODE")
@Entity(name = "verificationCode")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NUMBER(18)", name = "SERIAL", nullable = false)
    private Long serial;
    @Column(columnDefinition = "NVARCHAR2(300)", name = "SEED", nullable = false)
    private String seed;
    @Column(columnDefinition = "NUMBER(18)", name = "COUNTER", nullable = false)
    private Long counter;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}
