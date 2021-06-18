package io.reflectoring.coderadar.domain;

import lombok.Data;

@Data
public class AuthResponse {
  private String login;
  private String name;
  private String createdAt;
  private String token;
}
