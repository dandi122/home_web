package com.web.jkjk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.jkjk.entity.SnsUser;

@Repository
public interface UserRepository extends JpaRepository<SnsUser, Long>{

	Optional<SnsUser> findByUsername(String username);
}
