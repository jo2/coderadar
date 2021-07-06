package io.reflectoring.coderadar.plugin.api;

public interface WrappedAnalyzingProcess {
    void prepareAnalysis(String commit, String workDir, String buildCommand, String sonarUrl);

    void postprocessAnalysis(String commit, String sonarUrl);
}
