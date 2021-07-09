package io.reflectoring.coderadar.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestResponse {
    private Long id;
    private String title;
    private String description;
    private int points;
    private int coins;
}
