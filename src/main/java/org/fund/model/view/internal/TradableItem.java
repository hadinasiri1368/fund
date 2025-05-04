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

@Table(name = "vw_tradable_item")
@Entity(name = "tradableItem")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class TradableItem {
    @Id
    @Column(columnDefinition = "NUMBER", name = "ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(500)", name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(columnDefinition = "VARCHAR2(100)", name = "BOURSE_ACCOUNT")
    private Boolean bourseAccount;
    @Column(columnDefinition = "NUMBER", name = "TYPE_ID", nullable = false)
    private Long typeId;
    @Column(columnDefinition = "VARCHAR2(500)", name = "TYPE_NAME", nullable = false)
    private Long typeName;
    @Column(columnDefinition = "NUMBER", name = "TRADABLE_ITEM_GROUP", nullable = false)
    private Integer tradableItemGroup;
}