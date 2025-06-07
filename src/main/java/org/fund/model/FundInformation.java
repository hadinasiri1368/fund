package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_FUND_INFORMATION")
@Entity(name = "fundInformation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class FundInformation extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FUND_TYPE")
    private FileType fileType;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "NAME", nullable = false)
    private String name;
}
