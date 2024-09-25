package com.hospitalStaff.Security;


import com.hospitalStaff.Exception.AuthenticationNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isTokenInvalidated(String token) {
        try {
            String sql = "SELECT COUNT(*) FROM hospitalmicrosauth.invalidated_tokens WHERE token = ?";
           System.out.println("Executing SQL: " + sql + " with token: " + token); // Debugging
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{token}, Integer.class);
            return count != null && count > 0;
        } catch (BadSqlGrammarException e) {
            System.err.println("SQL Syntax Error: " + e.getMessage()); // Print the actual SQL error
            throw new RuntimeException("SQL syntax error in isTokenInvalidated method", e);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Generate token with roles prefixed with ROLE_
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();

        List<String> roles = principal.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) throws AuthenticationNotFoundException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get username from JWT
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Get roles from JWT
    public List<String> getRolesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }



}
