package it.mciasco.scadeora.security.persistence;


import it.mciasco.scadeora.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
