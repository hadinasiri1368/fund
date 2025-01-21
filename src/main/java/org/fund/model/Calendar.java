package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_CALENDAR")
@Entity(name = "calendar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Calendar extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "CHAR(10)", name = "CALENDAR_DATE", nullable = false)
    private String calendarDate;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_OFF", nullable = false)
    private Boolean isOff;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VACATION", nullable = false)
    private Boolean isVacation;
}
