package org.fund.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@SequenceGenerator(
        name = "hibernate_sequence",
        sequenceName = "hibernate_sequence",
        allocationSize = 1
)
public class BaseEntity implements Serializable {
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
