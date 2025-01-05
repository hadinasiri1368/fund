package org.fund.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_TEST")
@Entity(name = "test")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestNasiri extends BaseEntity implements Serializable {
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
}

