package dev.nicobgn.showcase.java.auth_demo.model;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
