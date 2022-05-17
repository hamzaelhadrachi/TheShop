package com.zerotohero.admin.user;

import com.zerotohero.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  role repository test
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    RoleRepository repository;

    @Test
    public void testCreateFirstRole(){

        Role adminRole  = new Role("Admin", "Manages Every Thing");
        Role savedRole = repository.save(adminRole);

        assertThat(savedRole.getId()).isGreaterThan(0);
    }

}