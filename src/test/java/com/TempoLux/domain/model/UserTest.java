package com.TempoLux.domain.model;

import com.TempoLux.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        User user = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@test.com")
                .password("encodedPassword")
                .memberId("TL-ABC123-CH")
                .tier("Gold")
                .preferredCollection("Alta Relojería")
                .role(UserRole.CLIENT)
                .joinedDate("Octubre 2024")
                .createdAt(LocalDateTime.of(2024, 10, 1, 10, 0))
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Juan Pérez");
        assertThat(user.getEmail()).isEqualTo("juan@test.com");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getMemberId()).isEqualTo("TL-ABC123-CH");
        assertThat(user.getTier()).isEqualTo("Gold");
        assertThat(user.getPreferredCollection()).isEqualTo("Alta Relojería");
        assertThat(user.getRole()).isEqualTo(UserRole.CLIENT);
        assertThat(user.getJoinedDate()).isEqualTo("Octubre 2024");
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 10, 1, 10, 0));
    }

    @Test
    void shouldCreateUserWithNoArgsConstructor() {
        User user = new User();
        user.setId(2L);
        user.setName("María García");
        user.setEmail("maria@test.com");
        user.setPassword("encodedPass");
        user.setMemberId("TL-XYZ789-CH");
        user.setTier("Silver");
        user.setPreferredCollection("Deportivo");
        user.setRole(UserRole.ADMIN);
        user.setJoinedDate("Noviembre 2024");
        user.setCreatedAt(LocalDateTime.of(2024, 11, 1, 12, 0));

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getName()).isEqualTo("María García");
        assertThat(user.getEmail()).isEqualTo("maria@test.com");
        assertThat(user.getPassword()).isEqualTo("encodedPass");
        assertThat(user.getMemberId()).isEqualTo("TL-XYZ789-CH");
        assertThat(user.getTier()).isEqualTo("Silver");
        assertThat(user.getPreferredCollection()).isEqualTo("Deportivo");
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(user.getJoinedDate()).isEqualTo("Noviembre 2024");
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 11, 1, 12, 0));
    }

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        User user = new User(
                3L,
                "Carlos López",
                "carlos@test.com",
                "encodedPass123",
                "TL-DEF456-CH",
                "Platinum",
                "Artesanal 3D",
                UserRole.CLIENT,
                "Diciembre 2024",
                LocalDateTime.of(2024, 12, 1, 14, 0)
        );

        assertThat(user.getId()).isEqualTo(3L);
        assertThat(user.getName()).isEqualTo("Carlos López");
        assertThat(user.getEmail()).isEqualTo("carlos@test.com");
        assertThat(user.getPassword()).isEqualTo("encodedPass123");
        assertThat(user.getMemberId()).isEqualTo("TL-DEF456-CH");
        assertThat(user.getTier()).isEqualTo("Platinum");
        assertThat(user.getPreferredCollection()).isEqualTo("Artesanal 3D");
        assertThat(user.getRole()).isEqualTo(UserRole.CLIENT);
        assertThat(user.getJoinedDate()).isEqualTo("Diciembre 2024");
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 12, 1, 14, 0));
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        User user1 = User.builder().id(1L).email("test@test.com").build();
        User user2 = User.builder().id(1L).email("test@test.com").build();
        User user3 = User.builder().id(2L).email("other@test.com").build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void shouldSupportToString() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .build();

        String toString = user.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("name=Test User");
        assertThat(toString).contains("email=test@test.com");
    }
}