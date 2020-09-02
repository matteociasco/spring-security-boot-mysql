package it.mciasco.scadeora.security.persistence;

import it.mciasco.scadeora.security.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);

}
