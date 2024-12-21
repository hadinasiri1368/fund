package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_FUND_INFORMATION")
@Entity(name = "fundInformation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundInformation extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FUND_TYPE")
    private FileType fileType;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "NAME", nullable = false)
    private String name;
}