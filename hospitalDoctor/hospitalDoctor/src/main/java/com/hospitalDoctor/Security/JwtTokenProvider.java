package com.hospitalDoctor.Security;


import com.hospitalDoctor.Exception.AuthenticationNotFoundException;
import com.hospitalDoctor.Exception.TokenInvalidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
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
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
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

    // Get expiration date from JWT
    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    // Check if the token is invalidated
    public boolean isTokenInvalidated(String token) {
        try {
            // SQL query to check if the token exists in the invalidated_tokens table
            String sql = "SELECT COUNT(*) FROM hospitalmicrosauth.invalidated_tokens WHERE token = ?";

            // Execute the query and get the result
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{token}, Integer.class);

            // Return true if the token exists in the invalidated tokens table, false otherwise
            return count != null && count > 0;

        } catch (BadSqlGrammarException e) {
            // Log the SQL syntax error and rethrow a custom runtime exception
            System.err.println("SQL Syntax Error: " + e.getMessage());
            throw new RuntimeException("SQL syntax error in isTokenInvalidated method", e);

        } catch (DataAccessException e) {
            // Log the database access exception (could be due to connection issues, etc.)
            System.err.println("Database Access Error: " + e.getMessage());
            throw new RuntimeException("Database access error in isTokenInvalidated method", e);

        } catch (Exception e) {
            // Log any other unexpected exceptions
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();

            // Return false as a fallback in case of an error (could adjust this based on your needs)
            return false;
        }
    }

}