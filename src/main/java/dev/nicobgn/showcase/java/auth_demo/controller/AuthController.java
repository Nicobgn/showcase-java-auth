package dev.nicobgn.showcase.java.auth_demo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nicobgn.showcase.java.auth_demo.model.AuthResponse;
import dev.nicobgn.showcase.java.auth_demo.model.RefreshRequest;
import dev.nicobgn.showcase.java.auth_demo.model.SigninRequest;
import dev.nicobgn.showcase.java.auth_demo.model.SignupRequest;
import dev.nicobgn.showcase.java.auth_demo.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
    return ResponseEntity.ok(authService.signup(request));
  }

  @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest request) {
    return ResponseEntity.ok(authService.signin(request));
  }

  @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshToken) {
    return ResponseEntity.ok(authService.refresh(refreshToken));
  }
}
