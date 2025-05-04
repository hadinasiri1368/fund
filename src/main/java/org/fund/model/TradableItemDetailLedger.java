package org.fund.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.tradableItem.TradableItemDetailLedgerDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_TRADABLE_ITEM_DETAIL_LEDGER")
@Entity(name = "tradableItemDetailLedger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class TradableItemDetailLedger extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NUMBER", name = "TRADABLE_ITEM_ID", nullable = false)
    private Long tradableItemId;
    @Column(columnDefinition = "NUMBER", name = "TRADABLE_ITEM_TYPE_ID", nullable = false)
    private Long tradableItemTypeId;
    @Column(columnDefinition = "NUMBER", name = "TRADABLE_ITEM_GROUP_ID", nullable = false)
    private Long tradableItemGroupId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
}
