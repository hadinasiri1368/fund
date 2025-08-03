package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.internal.TradableItem;

import java.io.Serializable;

@Table(name = "AHA_FUND_OWNERSHIP")
@Entity(name = "fundOwnership")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class FundOwnership extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRADABLE_ITEM_ID")
    private TradableItem tradableItem;
    @Column(columnDefinition = "NUMBER", name = "TRADABLE_ITEM_GROUP")
    private Long tradableItemGroup;
}
