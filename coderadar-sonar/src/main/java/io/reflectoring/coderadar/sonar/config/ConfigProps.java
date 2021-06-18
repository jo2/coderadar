package io.reflectoring.coderadar.sonar.config;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("coderadar")
@Data
public class ConfigProps {

  private static final Logger logger = LoggerFactory.getLogger(ConfigProps.class);
  private static final String CONFIG_PARAM_LOG_PATTERN = "{} is set to '{}'";

  @NotNull private String sonarUrl;

  @PostConstruct
  public void logConfig() {
    logger.info(CONFIG_PARAM_LOG_PATTERN, "coderadar.sonarUrl", this.sonarUrl);
  }
}
