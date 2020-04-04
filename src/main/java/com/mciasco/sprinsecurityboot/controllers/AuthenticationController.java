package com.mciasco.springsecurityboot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mciasco.springsecurityboot.domain.Role;
import com.mciasco.springsecurityboot.domain.User;
import com.mciasco.springsecurityboot.security.JwtAuthenticationRequest;
import com.mciasco.springsecurityboot.security.JwtAuthenticationResponse;
import com.mciasco.springsecurityboot.security.JwtTokenUtil;
import com.mciasco.springsecurityboot.services.RoleService;
import com.mciasco.springsecurityboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}") private String tokenHeader;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired UserService userService;
    @Autowired RoleService roleService;

    @PostMapping(value = "auth/login")
    public ResponseEntity<?> login(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws AuthenticationException, JsonProcessingException {
        // Effettuo l autenticazione
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Genero Token
        User user = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        response.setHeader(tokenHeader,token);
        // Ritorno il token
        return ResponseEntity.ok(new JwtAuthenticationResponse(user.getUsername(), token, user.getAuthorities()));
    }

    @PostMapping(value = "auth/register")
    public ResponseEntity<?> register(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws AuthenticationException, JsonProcessingException {

        Role userRole = roleService.getRole(Role.ROLE_USER);

        User user = User.builder()
                .username(authenticationRequest.getUsername())
                .password(passwordEncoder.encode(authenticationRequest.getPassword()))
                .enabled(Boolean.TRUE)
                .authorities(Arrays.asList(userRole))
                .build();

        userService.saveUser(user);

        final String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new JwtAuthenticationResponse(user.getUsername(), token, user.getAuthorities()));
    }

    @GetMapping(value = "token/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String token = request.getHeader(tokenHeader);
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            response.setHeader(tokenHeader,refreshedToken);
            return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(), token, (List<Role>) userDetails.getAuthorities()));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
