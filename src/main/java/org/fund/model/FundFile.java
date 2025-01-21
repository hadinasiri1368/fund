package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Blob;

@Table(name = "AHA_FUND_FILE")
@Entity(name = "fundFile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundFile extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FUND_ID")
    private Fund fund;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "FILE_NAME", nullable = false)
    private String fileName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FILE_TYPE_ID", nullable = false)
    private FileType fileType;
    @Column(name = "FILE_CONTENT", columnDefinition = "BLOB")
    @Lob
    private Blob fileContent;
    @Column(columnDefinition = "NVARCHAR2(300)", name = "DESCRIPTION")
    private String description;

}
