package org.fund.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

//@Table(name = "")
//@Entity(name = "")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@CacheableEntity
public class NetAssetValue {
    private Long id;
    private String calcDate;
}
