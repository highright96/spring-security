package dev.highright96.server.loginUserDetails.repository;

import dev.highright96.server.loginUserDetails.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}