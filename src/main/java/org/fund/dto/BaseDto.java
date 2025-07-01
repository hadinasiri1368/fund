package org.fund.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fund.repository.JpaRepository;

public abstract class BaseDto {
    @JsonIgnore
    protected transient JpaRepository repository;

    public void setRepository(JpaRepository repository) {
        this.repository = repository;
    }
}
