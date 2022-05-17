package com.zerotohero.admin.user;

import com.zerotohero.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *  role repository
 *
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
