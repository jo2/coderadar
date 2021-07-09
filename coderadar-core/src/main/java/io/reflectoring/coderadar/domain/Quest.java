package io.reflectoring.coderadar.domain;

import lombok.Data;

@Data
public class Quest {
    private Long id;
    private String title;
    private String description;
    private int points;
    private int coins;
}
