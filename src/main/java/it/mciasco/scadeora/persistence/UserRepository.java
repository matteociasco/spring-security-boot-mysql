package it.mciasco.scadeora.persistence;


import it.mciasco.scadeora.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
