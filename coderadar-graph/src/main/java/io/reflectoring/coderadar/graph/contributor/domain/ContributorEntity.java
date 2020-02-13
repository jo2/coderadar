package io.reflectoring.coderadar.graph.contributor.domain;

import io.reflectoring.coderadar.graph.projectadministration.domain.FileEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
public class ContributorEntity {
  private Long id;
  private String name;
  private Set<String> emails;

  @Relationship(type = "WORKS_ON")
  private List<ProjectEntity> projects;

  @Relationship(type = "MODIFIED")
  private List<FileEntity> files;
}
