package io.reflectoring.coderadar.analyzer.service;

import io.reflectoring.coderadar.plugin.api.AnalyzerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class SonarQubeService {

    private static final String HOST = "http://localhost:9000/";
    private static final String TOKEN = "564b0cdaa4b2b935648e3397ccf33d77cf224c32";
    private final RestTemplate restTemplate;

    public void prepareSonarAnalysis(long commitHash, String workDir) {
        // http://localhost:9000/web_api/api/projects/create
        String createQuery = HOST + "web_api/api/projects/create?name=" + commitHash + "&project=" + commitHash;
        restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders(TOKEN)), Object.class);

        String hostArg = " -D sonar.host.url=http://localhost:9000";
        String projectKeyArg = " -D sonar.projectKey=" + commitHash;
        String projectNameArg = " -D sonar.projectName=" + commitHash;
        String gitCommand = "git checkout " + commitHash;
        String sonarCommand = "sonar-scanner" + hostArg + projectKeyArg + projectNameArg;

        ProcessBuilder builder = new ProcessBuilder();
        // set command:
        //   execute in shell
        //   call git checkout commit <commitHash>
        //   call sonar-scanner <host> <projectKey> <projectName>
        builder.command("sh", "-c", gitCommand + " && " + sonarCommand);
        // set dir where to execute command
        builder.directory(new File(workDir));

        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            log.info("analysis finished with exit code: " + exitCode);
            if (exitCode != 0) {
                throw new AnalyzerException("Analysing process finished with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error during analysing process", e);
        }
    }

    public void cleanUpSonarAnalysis(long commitHash) {
        String createQuery = HOST + "web_api/api/projects/delete?project=" + commitHash;
        restTemplate.postForEntity(createQuery, new HttpEntity(createHeaders(TOKEN)), Object.class);
    }

    private HttpHeaders createHeaders(String token) {
        return new HttpHeaders() {{
            String auth = token + ":";
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
