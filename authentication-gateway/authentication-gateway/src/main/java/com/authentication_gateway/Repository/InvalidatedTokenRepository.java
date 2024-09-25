package com.authentication_gateway.Repository;

import com.authentication_gateway.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {

    Optional<InvalidatedToken>findByToken(String token);
}