package io.reflectoring.coderadar.sonar.adapter;

import io.reflectoring.coderadar.analyzer.port.driven.SonarQubePort;
import io.reflectoring.coderadar.sonar.config.ConfigProps;
import io.reflectoring.coderadar.sonar.domain.TokenResponse;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SonarQubeAdapter implements SonarQubePort {

  private final ConfigProps config;

  private String token = null;
  private final RestTemplate restTemplate = new RestTemplate();

  public void prepareSonarAnalysis(String commitHash, String workDir, String buildCommand) {
    restTemplate.postForEntity(
        config.getSonarUrl() + "api/user_tokens/revoke?name=" + commitHash,
        new HttpEntity(createHeaders()),
        String.class);
    ResponseEntity<TokenResponse> tokenResponse =
        restTemplate.postForEntity(
            config.getSonarUrl() + "api/user_tokens/generate?name=" + commitHash,
            new HttpEntity(createHeaders()),
            TokenResponse.class);
    token = tokenResponse.getBody().getToken();

    String createQuery =
        config.getSonarUrl() + "api/projects/create?name=" + commitHash + "&project=" + commitHash;
    restTemplate.exchange(
        createQuery, HttpMethod.POST, new HttpEntity(createHeaders()), String.class);

    String hostArg = " -D sonar.host.url=" + config.getSonarUrl();
    String projectKeyArg = " -D sonar.projectKey=" + commitHash;
    String projectNameArg = " -D sonar.projectName=" + commitHash;
    String tokenArg = " -D sonar.login=" + token;
    String gitCommand = "git checkout " + commitHash + " -f";
    String sonarCommand = "sonar-scanner" + hostArg + projectKeyArg + projectNameArg + tokenArg;

    ProcessBuilder builder = new ProcessBuilder();
    String command =
        gitCommand
            + " && "
            + (buildCommand != null ? buildCommand : getBuildCommand(workDir))
            + " && "
            + sonarCommand;
    log.info(command);
    builder.command("sh", "-c", command);
    // set dir where to execute command
    builder.directory(new File(workDir));

    try {
      builder.redirectError(ProcessBuilder.Redirect.INHERIT);
      builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
      Process process = builder.start();
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        log.error(
            "Preparation process finished with exit code {} for commit {}", exitCode, commitHash);
      } else {
        log.info(
            "Preparation process finished with exit code {} for commit {}", exitCode, commitHash);
      }
    } catch (IOException | InterruptedException e) {
      log.error("Error during analysing process", e);
    }
  }

  public void cleanUpSonarAnalysis(String commitHash) {
    log.info("remove project for commit {}", commitHash);
    String createQuery = config.getSonarUrl() + "api/projects/delete?project=" + commitHash;
    restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders()), String.class);
  }

  private HttpHeaders createHeaders() {
    return new HttpHeaders() {
      {
        String auth = "admin:coderadar";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }

  private String getBuildCommand(String workDir) {
    List<File> rootFolder = List.of(Objects.requireNonNull(new File(workDir).listFiles()));
    if (rootFolder.stream().map(File::getName).anyMatch(name -> name.equals("pom.xml"))) {
      // maven
      return "mvn clean compile";
    } else if (rootFolder.stream()
        .map(File::getName)
        .anyMatch(name -> name.equals("build.gradle"))) {
      // gradle
      return "gradle wrapper && ./gradlew build --stacktrace";
    } else {
      // java compile
      return "echo %PATH% && echo $PATH && java --version && javac **/*.java";
    }
  }
}
