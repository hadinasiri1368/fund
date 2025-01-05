package org.fund.model;


import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Table(name = "DETAIL_LEDGER")
@Entity(name = "detailLedger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailLedger extends BaseEntity implements Serializable {
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}

