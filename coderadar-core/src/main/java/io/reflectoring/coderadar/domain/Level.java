package io.reflectoring.coderadar.domain;

import lombok.Data;

@Data
public class Level {
    private int level;
    private int neededPoints;
    private int pointsForLevelUp;
}
