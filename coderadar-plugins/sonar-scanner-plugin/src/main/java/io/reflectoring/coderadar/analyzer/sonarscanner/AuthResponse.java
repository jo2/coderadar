package io.reflectoring.coderadar.analyzer.sonarscanner;

import lombok.Data;

@Data
public class AuthResponse {
    private String login;
    private String name;
    private String createdAt;
    private String token;
}
