package org.fund.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "hibernate_sequence")
    private Long id;
    @Column(name = "inserted_date_time", updatable = false)
    @JsonIgnore
    private Date insertedDateTime;
    @Column(name = "inserted_user_id", updatable = false)
    @JsonIgnore
    private Long insertedUserId;
    @Column(name = "updated_date_time")
    @JsonIgnore
    private Date updatedDateTime;
    @Column(name = "updated_user_id")
    @JsonIgnore
    private Long updatedUserId;
}