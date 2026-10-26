package com.TempoLux.domain.model;

import com.TempoLux.domain.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de dominio que representa a un cliente / miembro VIP de TempoLux.
 * Es agnóstica a la persistencia; su adaptador R2DBC hace el mapeo con la tabla "users".
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String memberId;
    private String tier;
    private String preferredCollection;
    private UserRole role;
    private String joinedDate;
    private LocalDateTime createdAt;
}
