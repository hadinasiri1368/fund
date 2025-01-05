package org.fund.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_TEST2")
@Entity(name = "test2")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestNasiri2 extends BaseEntity implements Serializable {
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
}

