package com.patient.security;

import com.patient.exception.AuthenticationNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extract the JWT token from the request
        String token = getJWTFromRequest(request);

        try {
            if (StringUtils.hasText(token)) {
                // First, check if the token has been invalidated
                if (tokenProvider.isTokenInvalidated(token)) {
                    // If token is invalidated, return unauthorized
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been invalidated");
                    return;
                }

                // If the token is not invalidated, validate it
                if (tokenProvider.validateToken(token)) {
                    String username = tokenProvider.getUsernameFromJWT(token);
                    List<String> roles = tokenProvider.getRolesFromJWT(token);

                    // Convert roles to GrantedAuthority
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // If token is invalid (not invalidated but failed validation), return unauthorized
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        } catch (AuthenticationNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Proceed with the filter chain if everything is fine
        filterChain.doFilter(request, response);
    }

    // Helper method to extract JWT from the request's Authorization header
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
