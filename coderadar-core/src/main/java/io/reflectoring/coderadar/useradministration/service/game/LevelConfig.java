package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.domain.Level;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "coderadar.game")
public class LevelConfig {

    private List<Level> levels;
}
