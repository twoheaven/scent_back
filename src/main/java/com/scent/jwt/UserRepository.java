package com.scent.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findOneWithAuthoritiesByUsername(String username);

}
