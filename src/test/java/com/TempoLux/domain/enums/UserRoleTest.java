package com.TempoLux.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        assertThat(UserRole.values()).containsExactly(UserRole.CLIENT, UserRole.VIP, UserRole.ADMIN);
    }

    @Test
    void shouldGetCorrectName() {
        assertThat(UserRole.CLIENT.name()).isEqualTo("CLIENT");
        assertThat(UserRole.VIP.name()).isEqualTo("VIP");
        assertThat(UserRole.ADMIN.name()).isEqualTo("ADMIN");
    }

    @Test
    void shouldGetCorrectOrdinal() {
        assertThat(UserRole.CLIENT.ordinal()).isEqualTo(0);
        assertThat(UserRole.VIP.ordinal()).isEqualTo(1);
        assertThat(UserRole.ADMIN.ordinal()).isEqualTo(2);
    }

    @Test
    void shouldValueOfCorrectly() {
        assertThat(UserRole.valueOf("CLIENT")).isEqualTo(UserRole.CLIENT);
        assertThat(UserRole.valueOf("VIP")).isEqualTo(UserRole.VIP);
        assertThat(UserRole.valueOf("ADMIN")).isEqualTo(UserRole.ADMIN);
    }
}