package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Blob;

@Table(name = "AHA_BANK")
@Entity(name = "bank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VALID", nullable = false)
    private Boolean isValid;
}
