package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_PARAMS_TYPE")
@Entity(name = "paramsType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamsType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "NAME", nullable = false)
    private String name;
}
