package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_ACCOUNT_NATURE")
@Entity(name = "accountNature")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class AccountNature extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(200)", name = "NAME", nullable = false)
    private String natureAccountName;
}
