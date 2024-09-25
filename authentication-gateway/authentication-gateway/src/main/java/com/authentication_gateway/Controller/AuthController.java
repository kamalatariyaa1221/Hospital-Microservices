package com.authentication_gateway.Controller;

import com.authentication_gateway.Entity.Role;
import com.authentication_gateway.Entity.User;
import com.authentication_gateway.Exception.AuthenticationNotFoundException;
import com.authentication_gateway.Payload.AuthResponse;
import com.authentication_gateway.Payload.LoginDto;
import com.authentication_gateway.Payload.SignUpDto;
import com.authentication_gateway.Payload.UserDetailsDto;
import com.authentication_gateway.Repository.RoleRepository;
import com.authentication_gateway.Repository.UserRepository;
import com.authentication_gateway.Security.JwtTokenProvider;
import com.authentication_gateway.Service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user--> / Sign-Up user-->

    @PostMapping("/registerUser")
    // http://localhost:9090/api/v1/auth/registerUser
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

        // Check if username exists in DB
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role role = roleRepository.findByName(signUpDto.getRoleType())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(role);
        user.setRoles(rolesSet);
        // Save user in the DB
        userRepository.save(user);

        return new ResponseEntity<>("Success..! User Registered Successfully !", HttpStatus.CREATED);
    }

    // login user--> / Sing In user-->

    @PostMapping("/signIn")
    // http://localhost:8080/api/v1/auth/signIn
    public ResponseEntity<AuthResponse> authorizeUser(@RequestBody LoginDto loginDto) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                            loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Generate JWT token
            String token = tokenProvider.generateToken(authentication);
            Date expirationDate = tokenProvider.getExpirationDateFromJWT(token);
            // Create response object
            AuthResponse authResponse = new AuthResponse(
                    token,
                    expirationDate,
                    "Success",
                    "Sign-In Successfully."
            );
            return ResponseEntity.ok(authResponse);
        }
        catch (AuthenticationException e) {
            // Handle authentication failure
            AuthResponse authResponse = new AuthResponse(
                    null,
                    null,
                    "Failure",
                    "Invalid !! Username or Password, Try again Later."
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }
    }

    // test a jwt bearer token provider-->

    @GetMapping("/testBearerToken")
    //    http://localhost:9090/api/v1/auth/testBearerToken
    public ResponseEntity<?> getUserDetailsbyBearerToken(HttpServletRequest request) throws AuthenticationNotFoundException, AuthenticationNotFoundException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token is null or does not start with Bearer");
            return new ResponseEntity<>( "Invalid token", HttpStatus.UNAUTHORIZED);
        }

        token = token.substring(7);
        System.out.println("Extracted Token: " + token);

        // Check if the token is invalidated (blacklisted)
        if (tokenProvider.isTokenInvalidated(token)) {  // Implement this method in TokenProvider
            System.out.println("Token is blacklisted or invalidated");
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        if (!tokenProvider.validateToken(token)) {
            System.out.println("Token validation failed");
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        String usernameOrEmail = tokenProvider.getUsernameFromJWT(token);
        System.out.println("Extracted Username/Email: " + usernameOrEmail);

        Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (!userOptional.isPresent()) {
            System.out.println("User not found for username/email: " + usernameOrEmail);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        System.out.println("User found: " + user.getUsername());

        UserDetailsDto userDetails = new UserDetailsDto(user.getUsername());

        return new ResponseEntity<>(userDetails,HttpStatus.OK);
    }

    @PostMapping("/logoutuser")
    // http://localhost:9090/api/v1/auth/logoutuser
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        tokenProvider.invalidateToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }

//    public ResponseEntity<?>logout(HttpServletRequest request) {
//        String token = getJWTFromRequest(request);
//        if (token != null && !token.isEmpty()) {
//            tokenProvider.invalidateToken(token);
//            return ResponseEntity.ok("Logout successful");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
//    }
//
//

}
