package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.domain.Level;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coderadar.game")
public class LevelConfig {

  private List<Level> levels;
}
