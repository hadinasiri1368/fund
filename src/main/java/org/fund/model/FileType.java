package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_FILE_TYPE")
@Entity(name = "fileType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class FileType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
}
