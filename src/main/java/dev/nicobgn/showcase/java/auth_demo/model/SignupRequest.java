package dev.nicobgn.showcase.java.auth_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
  private String username;
  private String password;
}
