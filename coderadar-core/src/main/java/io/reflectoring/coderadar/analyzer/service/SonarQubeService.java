package io.reflectoring.coderadar.analyzer.service;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.domain.AuthResponse;
import io.reflectoring.coderadar.plugin.api.AnalyzerException;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SonarQubeService {

  private final CoderadarConfigurationProperties coderadarConfigurationProperties;

  private String token = "fcd207855a530bbc7127109f56d8dc0a3ced6662";
  private final RestTemplate restTemplate = new RestTemplate();

  public void prepareSonarAnalysis(String commitHash, String workDir) {
    String createQuery =
        coderadarConfigurationProperties.getSonarUrl() + "web_api/api/projects/create?name=" + commitHash + "&project=" + commitHash;
    restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders(token)), String.class);

    String hostArg = " -D sonar.host.url=" + coderadarConfigurationProperties.getSonarUrl();
    String projectKeyArg = " -D sonar.projectKey=" + commitHash;
    String projectNameArg = " -D sonar.projectName=" + commitHash;
    String tokenArg = " -D sonar.login=" + token;
    String gitCommand = "git checkout " + commitHash + " -f";
    String sonarCommand = "sonar-scanner" + hostArg + projectKeyArg + projectNameArg + tokenArg;

    ProcessBuilder builder = new ProcessBuilder();
    String command = gitCommand + " && " + getBuildCommand(workDir) + " && " + sonarCommand;
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
        log.error("Preparation process finished with exit code {} for commit {}", exitCode, commitHash);
      } else {
        log.info("Preparation process finished with exit code {} for commit {}", exitCode, commitHash);
      }
    } catch (IOException | InterruptedException e) {
      log.error("Error during analysing process", e);
    }
  }

  public void cleanUpSonarAnalysis(long commitHash) {
    String createQuery = coderadarConfigurationProperties.getSonarUrl() + "web_api/api/projects/delete?project=" + commitHash;
    restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders(token)), String.class);
  }

  private HttpHeaders createHeaders(String token) {
    return new HttpHeaders() {
      {
        String auth = token + ":";
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
    } else if (rootFolder.stream().map(File::getName).anyMatch(name -> name.equals("build.gradle"))) {
      // gradle
      return "gradle wrapper && ./gradlew clean build --stacktrace --info";
    } else {
      // java compile
      return "javac **/*.java";
    }
  }
}
