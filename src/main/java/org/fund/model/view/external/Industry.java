package org.fund.model.view.external;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.BaseEntity;

import java.io.Serializable;

@Table(name = "INDUSTRY")
@Entity(name = "industry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CacheableEntity
public class Industry {
    @Id
    @Column(columnDefinition = "NUMBER", name = "INDUSTRY_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(20)", name = "INDUSTRY_CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "VARCHAR2(200)", name = "FARSI_NAME", nullable = false)
    private String name;

}