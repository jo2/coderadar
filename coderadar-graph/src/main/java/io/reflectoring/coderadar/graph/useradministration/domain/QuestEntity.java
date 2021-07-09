package io.reflectoring.coderadar.graph.useradministration.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class QuestEntity {
    private Long id;
    private String title;
    private String description;
    private int points;
    private int coins;
}
