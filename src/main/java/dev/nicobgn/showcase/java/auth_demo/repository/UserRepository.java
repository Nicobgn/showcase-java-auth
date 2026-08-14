package dev.nicobgn.showcase.java.auth_demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.nicobgn.showcase.java.auth_demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
}
