package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_MENU")
@Entity(name = "menu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "NAME", columnDefinition = "NVARCHAR2(50)", nullable = false)
    private String name;
    @Column(name = "MENU_LEVEL", columnDefinition = "NUMBER(10)", nullable = false)
    private Integer menuLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_MENU_ID")
    private Menu menu;
}
