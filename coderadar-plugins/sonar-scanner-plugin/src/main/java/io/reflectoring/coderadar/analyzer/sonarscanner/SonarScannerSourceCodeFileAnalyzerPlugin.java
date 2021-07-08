package io.reflectoring.coderadar.analyzer.sonarscanner;

import io.reflectoring.coderadar.plugin.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SonarScannerSourceCodeFileAnalyzerPlugin
    implements SourceCodeFileAnalyzerPlugin, WrappedAnalyzingProcess {

  private final RestTemplate restTemplate = new RestTemplate();

  private static final String BASE_URL = "http://sonarqube:9000/api/measures/component_tree";

  private static final List<String> METRICS =
      List.of(
          "test_success_density", // -> Zuverlässigkeit, Reifegrad | done
          "uncovered_lines", // -> Zuverlässigkeit, Reifegrad | done
          "cognitive_complexity", // -> Analysierbarkeit, Reifegrad | done
          "coverage", // -> Reifegrad | done
          "line_coverage", // -> Reifegrad | done
          "sqale_rating", // -> Wartbarkeit | done
          "vulnerabilities", // -> Reifegrad | done
          "public_undocumented_api", // -> Kompatibilität, Analysierbarkeit | done
          "effort_to_reach_maintainability_rating_a", // -> Reifegrad | done
          "duplicated_lines_density", // -> Wiederverwendbarkeit | done
          "code_smells", // -> Reifegrad | done
          "bugs", // -> Sicherheit, Zuverlässigkeit | done
          "reliability_rating", // -> Zuverlässigkeit | done
          "violations", // -> Zuverlässigkeit | done
          "lines_to_cover" // -> Zuverlässigkeit, Reifegrad | done
          );
  private static final String METRICS_STRING = "?metricKeys=" + String.join(",", METRICS);
  private static final String STRATEGY = "&strategy=leaves";
  private static final String PAGE_SIZE = "&ps=500";

  private final Map<String, Component> componentMap = new HashMap<>();

  @Override
  public AnalyzerFileFilter getFilter() {
    return filename -> filename.endsWith(".java");
  }

  @Override
  public FileMetrics analyzeFile(String filepath, byte[] fileContent) {
    if (!componentMap.containsKey(filepath)) {
      log.debug("No results for {}", filepath);
      return new FileMetrics();
    }
    try {
      Component fileComponent = this.componentMap.get(filepath);
      FileMetrics fileMetrics = new FileMetrics();
      fileComponent
          .getMeasures()
          .forEach(
              measure ->
                  fileMetrics.addFinding(
                      new Metric("sonarscanner:" + measure.getMetric()),
                      new Finding(0, 0, ""),
                      (int) Math.round(measure.getValue())));
      return fileMetrics;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new FileMetrics();
  }

  private HttpHeaders createHeaders() {
    return new HttpHeaders() {
      {
        String auth = "admin:admin";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }

  @Override
  public void prepareAnalysis(String commit, String workDir, String buildCommand, String sonarUrl) {
    log.debug("Prepare analysis for commit {}", commit);
    String token = createProject(commit, sonarUrl);
    buildAndAnalyze(commit, sonarUrl, buildCommand, workDir, token);

    RestTemplate restTemplate = new RestTemplate();
    try {
      ResponseEntity<MeasureResponse> call =
          restTemplate.exchange(
              BASE_URL + METRICS_STRING + STRATEGY + PAGE_SIZE + "&component=" + commit,
              HttpMethod.GET,
              new HttpEntity<>(createHeaders()),
              MeasureResponse.class);

      if (!call.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        MeasureResponse pageOneResponse = call.getBody();
        Objects.requireNonNull(pageOneResponse)
            .getComponents()
            .forEach(component -> this.componentMap.put(component.getPath(), component));

        double pageCount =
            Math.ceil(
                pageOneResponse.getPaging().getTotal() / pageOneResponse.getPaging().getPageSize());
        for (int index = 2; index <= pageCount; index++) {
          MeasureResponse nextPageResponse =
              restTemplate
                  .exchange(
                      BASE_URL
                          + METRICS
                          + STRATEGY
                          + PAGE_SIZE
                          + "&component="
                          + commit
                          + "&p="
                          + index,
                      HttpMethod.GET,
                      new HttpEntity<>(createHeaders()),
                      MeasureResponse.class)
                  .getBody();
          Objects.requireNonNull(nextPageResponse)
              .getComponents()
              .forEach(component -> this.componentMap.put(component.getPath(), component));
        }
        log.info("Files found for commit {}: {}", commit, this.componentMap.size());
      } else {
        log.error("Commit not analyzed: {}", commit);
      }

    } catch (RestClientException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void postprocessAnalysis(String commit, String sonarUrl) {
    log.debug("Remove project for commit {}", commit);
    String createQuery = sonarUrl + "api/projects/delete?project=" + commit;
    restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders()), String.class);
  }

  private String createProject(String commit, String sonarUrl) {
    restTemplate.postForEntity(
        sonarUrl + "api/user_tokens/revoke?name=" + commit,
        new HttpEntity(createHeaders()),
        String.class);
    ResponseEntity<TokenResponse> tokenResponse =
        restTemplate.postForEntity(
            sonarUrl + "api/user_tokens/generate?name=" + commit,
            new HttpEntity(createHeaders()),
            TokenResponse.class);
    String token = tokenResponse.getBody().getToken();

    String createQuery = sonarUrl + "api/projects/create?name=" + commit + "&project=" + commit;

    try {
      restTemplate.exchange(
              createQuery, HttpMethod.POST, new HttpEntity(createHeaders()), String.class);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST) && e.getMessage() != null &&
              e.getMessage().contains("Could not create Project, key already exists")) {
        log.debug("Project for commit {} already exists, not recreating it", commit);
      } else {
        log.error("Error creating project for commit {}", commit, e);
      }
    }
    return token;
  }

  private void buildAndAnalyze(
      String commit, String sonarUrl, String buildCommand, String workDir, String token) {
    String hostArg = " -D sonar.host.url=" + sonarUrl;
    String projectKeyArg = " -D sonar.projectKey=" + commit;
    String projectNameArg = " -D sonar.projectName=" + commit;
    String tokenArg = " -D sonar.login=" + token;
    String sonarCommand = "sonar-scanner" + hostArg + projectKeyArg + projectNameArg + tokenArg;

    try /*(Git git = Git.open(new File(workDir)))*/ {
      ProcessBuilder gitBuilder = new ProcessBuilder();
      gitBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
      gitBuilder.command("sh", "-c", "git checkout -f " + commit);
      gitBuilder.directory(new File(workDir));
      Process gitProcess = gitBuilder.start();
      int gitCode = gitProcess.waitFor();
      if (gitCode != 0) {
        log.error("Checkout finished with exit code {} for commit {}", gitCode, commit);
      } else {
        log.debug("Checkout finished with exit code {} for commit {}", gitCode, commit);
      }

//      git.checkout().setName(commit).setForced(true).call();

      ProcessBuilder builder = new ProcessBuilder();
      builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
      builder.redirectError(ProcessBuilder.Redirect.INHERIT);
      String command =
          (buildCommand != null ? formatCommand(workDir, buildCommand) : getBuildCommand(workDir))
              + " && "
              + sonarCommand;
      log.debug("Analyse commit using '{}'", command);
      builder.command("sh", "-c", command);
      builder.directory(new File(workDir));
      Process process = builder.start();
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        log.error("Preparation process finished with exit code {} for commit {}", exitCode, commit);
      } else {
        log.debug("Preparation process finished with exit code {} for commit {}", exitCode, commit);
      }
    } catch (IOException | InterruptedException e) {
      log.error("Error during analysing process", e);
    } /*catch (GitAPIException e) {
      log.error("Error during git checkout", e);
    }*/
  }

  private String formatCommand(String workDir, String command) {
    List<File> rootFolder = List.of(Objects.requireNonNull(new File(workDir).listFiles()));
    Optional<File> buildGradle =
        rootFolder.stream().filter(file -> file.getName().equals("build.gradle")).findFirst();

    if (buildGradle.isPresent()) {
      if (command.startsWith("./gradlew")
          && rootFolder.stream().map(File::getName).anyMatch(name -> name.equals("gradlew"))) {
        command = "chmod +x ./gradlew && " + command;
      }
      if (command.startsWith("./gradlew")
          && rootFolder.stream().map(File::getName).noneMatch(name -> name.equals("gradlew"))) {
        command = "gradle wrapper && " + command;
      }

      try {
        String javaVersion;
        List<String> lines = Files.readAllLines(Path.of(buildGradle.get().getAbsolutePath()));
        String sourceCompatibility =
            lines.stream()
                .filter(
                    line ->
                        line.contains("sourceCompatibility")
                            || line.contains("targetCompatibility"))
                .map(line -> line.substring(line.indexOf("=") + 1))
                .findFirst()
                .orElse(null);

        String languageVersion =
            lines.stream()
                .filter(line -> line.contains("languageVersion"))
                .map(
                    line ->
                        line.substring(
                            line.indexOf("JavaLanguageVersion.of(") + 1, line.indexOf(")")))
                .findFirst()
                .orElse(null);

        if (sourceCompatibility != null) {
          javaVersion = sourceCompatibility.replaceAll("\\s", "");
          if (javaVersion.equals("1.7")) {
            javaVersion = "1.8";
          }
        } else if (languageVersion != null) {
          switch (languageVersion) {
            case "7":
              javaVersion = "1.7";
              break;
            case "8":
              javaVersion = "1.8";
              break;
            default:
              javaVersion = languageVersion;
          }
        } else {
          javaVersion = "1.8";
        }

        log.debug("Compile using JAVA {}", javaVersion);
        String javaHome = "/usr/lib/jvm/java-" + javaVersion + "-openjdk";
        String path =
            javaHome
                + "/bin:/opt/sonar-scanner/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin";
        command = "export JAVA_HOME=" + javaHome + " && export PATH=" + path + " && " + command;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return command;
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
      return "ls -l && java --version && javac **/*.java";
    }
  }
}
