package io.reflectoring.coderadar.analyzer.port.driven;

public interface SonarQubePort {
  void prepareSonarAnalysis(String commitHash, String workDir, String buildCommand);

  void cleanUpSonarAnalysis(String commitHash);
}
