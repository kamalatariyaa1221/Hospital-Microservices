package com.hospitalDepartment.Repository;

import com.hospitalDepartment.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {

    Optional<InvalidatedToken> findByToken(String token);
}