package io.reflectoring.coderadar.sonar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
  private String login;
  private String name;
  private String createdAt;
  private String token;
}
