package com.authentication_gateway.Config;

import com.authentication_gateway.Security.JwtAuthenticationFilter;
import com.authentication_gateway.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private  CustomUserDetailsService userDetailsService;

    private  final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/v1/patient/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/staff/addStaffByDepartmentId/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/staff/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/doctor/addDoctor/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/staff/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/department/createDepartment/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/department/**").permitAll()

                        .anyRequest().authenticated()
                        )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session to stateless
                )


                .formLogin(form->form
                        .loginPage("/login")
                        .permitAll())
                .httpBasic(withDefaults());
        return http.build();
    }
}
