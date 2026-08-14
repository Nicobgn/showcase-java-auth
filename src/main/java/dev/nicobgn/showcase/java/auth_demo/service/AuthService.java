package dev.nicobgn.showcase.java.auth_demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.nicobgn.showcase.java.auth_demo.model.AuthResponse;
import dev.nicobgn.showcase.java.auth_demo.model.RefreshRequest;
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

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  @Transactional
  public AuthResponse signin(SigninRequest request) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  @Transactional
  public AuthResponse refresh(RefreshRequest request) {
    String refreshToken = request.getRefreshToken();

    if (refreshToken == null || refreshToken.isEmpty()) {
      throw new IllegalArgumentException("Refresh token cannot be empty");
    }

    String username = jwtService.extractUsername(refreshToken);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!jwtService.isTokenValid(refreshToken, user)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    String newAccessToken = jwtService.generateAccessToken(user);

    return AuthResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshToken) // Keep the same refresh token
        .build();
  }
}
