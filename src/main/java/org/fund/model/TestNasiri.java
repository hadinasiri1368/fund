package org.fund.model;


import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_TEST")
@Entity(name = "test")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class TestNasiri extends BaseEntity implements Serializable {
    @Column(columnDefinition = "NVARCHAR2(12)", nullable = false)
    private String name;
}

