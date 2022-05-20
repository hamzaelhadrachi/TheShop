package com.zerotohero.admin.user;

import com.zerotohero.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  role repository test
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class RoleRepositoryTest {

    @Autowired
    RoleRepository repository;

    @Test
    public void testCreateRestOfRoles(){

        Role adminRole  = new Role("Admin", "Manages Every Thing");
        Role salesPersonRole  = new Role("SalesPerson", "manages ProductPrice, Customers, Shipping, Orders and Sales Reports.");
        Role editorRole  = new Role("Editor", "manages Categories, Brands, Products, Articles and Menus.");
        Role shipperRole  = new Role("Shipper", "view products, view orders and update order status.");
        Role assistantRole  = new Role("Assistant", "manages questions and reviews.");
        Iterable<Role> savedRoles = repository.saveAll(Arrays.asList(adminRole, salesPersonRole, editorRole, shipperRole, assistantRole));

        savedRoles.forEach(
                role -> assertThat(role.getId()).isGreaterThan(0)
        );
    }

}