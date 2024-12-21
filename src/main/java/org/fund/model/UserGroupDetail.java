package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_USER_GROUP_DETAIL")
@Entity(name = "userGroupDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "F_USER_ID")
    private Users users;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_GROUP_ID")
    private UserGroup userGroup;
}
