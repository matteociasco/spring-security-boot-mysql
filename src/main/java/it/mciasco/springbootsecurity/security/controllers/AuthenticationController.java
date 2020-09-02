package it.mciasco.scadeora.security.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.mciasco.scadeora.security.domain.Role;
import it.mciasco.scadeora.security.domain.User;
import it.mciasco.scadeora.security.dtos.JwtAuthenticationRequest;
import it.mciasco.scadeora.security.dtos.JwtAuthenticationResponse;
import it.mciasco.scadeora.security.services.RoleService;
import it.mciasco.scadeora.security.services.UserService;
import it.mciasco.scadeora.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}") private String tokenHeader;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    final PasswordEncoder passwordEncoder;

    final UserService userService;
    final RoleService roleService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

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
        List<String> authorities = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtAuthenticationResponse(user.getUsername(), token, authorities));
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
                .authorities(Collections.singletonList(userRole))
                .build();

        userService.saveUser(user);

        final String token = jwtTokenUtil.generateToken(user);

        List<String> authorities = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtAuthenticationResponse(user.getUsername(), token, authorities));
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
            List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(), token, authorities));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
