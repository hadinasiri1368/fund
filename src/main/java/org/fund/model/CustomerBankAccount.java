package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_CUSTOMER_BANK_ACCOUNT")
@Entity(name = "customerBankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class CustomerBankAccount extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_CUSTOMER_ID")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount;
}
