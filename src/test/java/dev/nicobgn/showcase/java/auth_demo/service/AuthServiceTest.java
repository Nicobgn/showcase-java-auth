package dev.nicobgn.showcase.java.auth_demo.service;

import dev.nicobgn.showcase.java.auth_demo.model.AuthResponse;
import dev.nicobgn.showcase.java.auth_demo.model.SignupRequest;
import dev.nicobgn.showcase.java.auth_demo.model.User;
import dev.nicobgn.showcase.java.auth_demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthService authService;

  private SignupRequest signupRequest;

  @BeforeEach
  void setUp() {
    signupRequest = new SignupRequest("tester", "password123");
  }

  @Test
  @DisplayName("Debe registrar un nuevo usuario y generar access y refresh tokens")
  void signup_ShouldCreateUserAndGenerateBothTokens_WhenUsernameIsAvailable() {
    // Arrange
    when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
    when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

    // Simular la generación de ambos tokens
    when(jwtService.generateAccessToken(any(User.class))).thenReturn("generatedAccessToken");
    when(jwtService.generateRefreshToken(any(User.class))).thenReturn("generatedRefreshToken");

    // Act
    AuthResponse response = authService.signup(signupRequest);

    // Assert
    assertNotNull(response);
    assertEquals("generatedAccessToken", response.getAccessToken());
    assertEquals("generatedRefreshToken", response.getRefreshToken());

    // Verificar que se guardó el usuario y que ambos métodos del token se invocaron
    verify(userRepository, times(1)).save(any(User.class));
    verify(jwtService, times(1)).generateAccessToken(any(User.class));
    verify(jwtService, times(1)).generateRefreshToken(any(User.class));
  }

  @Test
  @DisplayName("Debe lanzar excepción cuando el username ya existe")
  void signup_ShouldThrowException_WhenUsernameAlreadyExists() {
    // Arrange
    when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> authService.signup(signupRequest));

    assertEquals("Username already exists", exception.getMessage());
    verify(userRepository, never()).save(any(User.class));
    verify(jwtService, never()).generateAccessToken(any(User.class));
    verify(jwtService, never()).generateRefreshToken(any(User.class));
  }
}