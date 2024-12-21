package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_ACCOUNT_NATURE")
@Entity(name = "accountNature")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountNature extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NATURE_ACCOUNT_NAME", nullable = false)
    private String natureAccountName;
}