package it.mciasco.scadeora.persistence;

import it.mciasco.scadeora.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);

}
