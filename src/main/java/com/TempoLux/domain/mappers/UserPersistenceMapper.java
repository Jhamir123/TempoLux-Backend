package com.TempoLux.domain.mappers;

import com.TempoLux.domain.enums.UserRole;
import com.TempoLux.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre la entidad de dominio User (model) y el modelo de persistencia R2DBC User (entity).
 */
@Component
public class UserPersistenceMapper {

    public User toDomain(com.TempoLux.domain.entity.UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .memberId(entity.getMemberId())
                .tier(entity.getTier())
                .preferredCollection(entity.getPreferredCollection())
                .role(UserRole.valueOf(entity.getRole()))
                .joinedDate(entity.getJoinedDate())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public com.TempoLux.domain.entity.UserEntity toPersistence(User user) {
        if (user == null) {
            return null;
        }
        UserRole role = user.getRole() == null ? UserRole.CLIENT : user.getRole();
        return com.TempoLux.domain.entity.UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .memberId(user.getMemberId())
                .tier(user.getTier())
                .preferredCollection(user.getPreferredCollection())
                .role(role.name())
                .joinedDate(user.getJoinedDate())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
