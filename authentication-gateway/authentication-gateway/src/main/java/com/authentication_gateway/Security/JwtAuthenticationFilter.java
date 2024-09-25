package com.authentication_gateway.Security;




import com.authentication_gateway.Exception.AuthenticationNotFoundException;
import com.authentication_gateway.Service.CustomUserDetailsService;
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
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJWTfromRequest(request);
        if (StringUtils.hasText(token)) {
            try {
                if (tokenProvider.validateToken(token) && !tokenProvider.isTokenInvalidated(token)) {
                    String username = tokenProvider.getUsernameFromJWT(token);
                    List<String> roles = tokenProvider.getRolesFromJWT(token);

                    // Convert roles into GrantedAuthority
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return;
                }
            } catch (AuthenticationNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String token = getJWTfromRequest(request);
//        if (StringUtils.hasText(token)) {
//            try {
//                if (tokenProvider.validateToken(token)) {
//                    String username = tokenProvider.getUsernameFromJWT(token);
//                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//                    List<String> roles = tokenProvider.getRolesFromJWT(token);
//
//                    // Convert roles into GrantedAuthority
//                    List<GrantedAuthority> authorities = roles.stream()
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());
//
//                    // Create Authentication object
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, authorities);
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                } else {
//                    System.out.println("JWT validation failed");
//                }
//            } catch (AuthenticationNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        filterChain.doFilter(request, response);
//
//    }


    // Bearer <accessToken>
    private String getJWTfromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader( "Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer: ")) {
//            return bearerToken.substring(8, bearerToken.length());
//        }
//        return  null;
        String bearerToken = request.getHeader("Authorization");

        // Check if the token starts with "Bearer " (including the space)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extract the token by removing the "Bearer " prefix
            return bearerToken.substring(7);
        }
        return null;
    }
}
