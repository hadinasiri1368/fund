package org.fund.model.view.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "VW_SECURITY")
@Entity(name = "security")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class Security {
    @Id
    @Column(columnDefinition = "NUMBER", name = "ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(500)", name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(columnDefinition = "VARCHAR2(100)", name = "BOURSE_ACCOUNT", nullable = false)
    private Boolean bourseAccount;
    @Column(columnDefinition = "NUMBER", name = "SECURITY_TYPE_ID", nullable = false)
    private Long securityTypeId;
    @Column(columnDefinition = "VARCHAR2(500)", name = "SECURITY_TYPE_NAME", nullable = false)
    private Long securityTypeName;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_CONTRACT", nullable = false)
    private Boolean isContract;
}