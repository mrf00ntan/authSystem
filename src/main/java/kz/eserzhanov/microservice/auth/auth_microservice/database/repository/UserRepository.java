package kz.eserzhanov.microservice.auth.auth_microservice.database.repository;

import kz.eserzhanov.microservice.auth.auth_microservice.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
