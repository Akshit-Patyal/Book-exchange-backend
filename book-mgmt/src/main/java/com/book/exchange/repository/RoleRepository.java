package com.book.exchange.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.exchange.entity.Role;
import com.book.exchange.enums.ERole;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
