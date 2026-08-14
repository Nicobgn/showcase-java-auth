package dev.nicobgn.showcase.java.auth_demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.nicobgn.showcase.java.auth_demo.model.AuthResponse;
import dev.nicobgn.showcase.java.auth_demo.model.Role;
import dev.nicobgn.showcase.java.auth_demo.model.SigninRequest;
import dev.nicobgn.showcase.java.auth_demo.model.SignupRequest;
import dev.nicobgn.showcase.java.auth_demo.model.User;
import dev.nicobgn.showcase.java.auth_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if (request.getUsername() == null || request.getUsername().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    userRepository.save(user);

    String jwtToken = jwtService.generateToken(user);
    return AuthResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(jwtToken)
        .build();
  }

  @Transactional
  public AuthResponse signin(SigninRequest request) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    String jwtToken = jwtService.generateToken(user);
    return AuthResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(jwtToken)
        .build();
  }
}
