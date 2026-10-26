package com.TempoLux.domain.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Modelo de persistencia R2DBC que refleja la tabla "users".
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    private Long id;

    private String name;
    private String email;
    private String password;

    @Column("member_id")
    private String memberId;

    private String tier;

    @Column("preferred_collection")
    private String preferredCollection;

    private String role;

    @Column("joined_date")
    private String joinedDate;

    @Column("created_at")
    private LocalDateTime createdAt;
}
