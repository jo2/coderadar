package io.reflectoring.coderadar.graph.useradministration.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public class BadgeEntity {
    private Long id;
    private String title;
    private int amountNeeded;
    private int currentAmount;
    private String metric;
}
