package io.reflectoring.coderadar.graph.useradministration.repository;

import io.reflectoring.coderadar.graph.useradministration.domain.QuestEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface QuestRepository extends Neo4jRepository<QuestEntity, Long> {
}
