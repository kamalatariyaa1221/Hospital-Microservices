package com.authentication_gateway.Security;

import com.authentication_gateway.Exception.AuthenticationNotFoundException;
import com.authentication_gateway.Service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    private Set<String> invalidatedTokens = new HashSet<>();


    @Autowired
    private CustomUserDetailsService userDetails;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to invalidate a token
    public void invalidateToken(String token) {
        Date expirationDate = getExpirationDateFromJWT(token);
        Date createdAt = new Date(); // Current timestamp
        String sql = "INSERT INTO invalidated_tokens (token, expiration_date, created_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, token, expirationDate, createdAt);
    }


    public boolean isTokenInvalidated(String token) {
        String sql = "SELECT COUNT(*) FROM invalidated_tokens WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, token);
        return count != null && count > 0;
    }

    // generate token

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String usernameOrEmail = principal.getUsername(); // This could be either username or email
        List<String> roles = principal.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()) // Roles are already prefixed with ROLE_
                .collect(Collectors.toList());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(usernameOrEmail)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // validate JWT token

    public boolean validateToken(String token) throws AuthenticationNotFoundException {

        try{
            if (isTokenInvalidated(token))
                return false;
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    // extract the roles from JWT Token

    public List<String> getRolesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    // extract username from the JWT Token

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // get ExpiredDate and Time For JWT Token

    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }


    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}