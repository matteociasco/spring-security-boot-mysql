package com.mciasco.springsecurityboot.persistence;

import com.mciasco.springsecurityboot.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);

}
