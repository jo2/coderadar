package io.reflectoring.coderadar.analyzer.sonarscanner;

import io.reflectoring.coderadar.plugin.api.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SonarScannerSourceCodeFileAnalyzerPlugin
    implements SourceCodeFileAnalyzerPlugin, ConfigurableAnalyzerPlugin {

  private static final String BASE_URL = "http://sonarqube:9000/api/measures/component_tree";

  private static final List<String> METRICS =
      List.of(
          "test_success_density", // -> Zuverlässigkeit, Reifegrad
          "uncovered_lines", // -> Zuverlässigkeit, Reifegrad
          "cognitive_complexity", // -> Analysierbarkeit, Reifegrad
          "coverage", // -> Reifegrad
          "line_coverage", // -> Reifegrad
          "sqale_rating", // -> Wartbarkeit
          "vulnerabilities", // -> Reifegrad
          "public_undocumented_api", // -> Kompatibilität, Analysierbarkeit
          "effort_to_reach_maintainability_rating_a", // -> Reifegrad
          "duplicated_lines_density", // -> Wiederverwendbarkeit
          "code_smells", // -> Reifegrad
          "bugs", // -> Sicherheit, Zuverlässigkeit
          "reliability_rating", // -> Zuverlässigkeit
          "violations", // -> Zuverlässigkeit
          "lines_to_cover" // -> Zuverlässigkeit, Reifegrad
          );
  private static final String METRICS_STRING = "?metricKeys=" + String.join(",", METRICS);
  private static final String STRATEGY = "&strategy=leaves";
  private static final String PAGE_SIZE = "&ps=500";

  private final Map<String, Component> componentMap = new HashMap<>();

  private void init(String projectName) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      ResponseEntity<MeasureResponse> call =
          restTemplate.exchange(
              BASE_URL + METRICS_STRING + STRATEGY + PAGE_SIZE + "&component=" + projectName,
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
                          + projectName
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
        log.info("Files found: {}", this.componentMap.size());
      } else {
        log.error("Commit not analyzed: {}", projectName);
      }

    } catch (RestClientException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AnalyzerFileFilter getFilter() {
    return filename -> filename.endsWith(".java");
  }

  @Override
  public FileMetrics analyzeFile(String filepath, byte[] fileContent) {
    if (!componentMap.containsKey(filepath)) {
      log.info("No results for {}", filepath);
      return new FileMetrics();
    }
    System.out.println(filepath);
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
        String auth = "admin:coderadar";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }

  @Override
  public boolean isValidConfigurationFile(byte[] configurationFile) {
    return configurationFile != null && configurationFile.length > 0;
  }

  @Override
  public void configure(byte[] configurationFile) {
    init(new String(configurationFile, StandardCharsets.UTF_8));
  }
}
