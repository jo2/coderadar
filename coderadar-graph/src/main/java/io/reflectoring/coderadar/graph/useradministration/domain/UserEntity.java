package io.reflectoring.coderadar.graph.useradministration.domain;

import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.graph.contributor.domain.ContributorEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import java.util.List;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/** @see User */
@NodeEntity
@Data
public class UserEntity {
  private Long id;
  private String username;
  private String password;
  private boolean platformAdmin = false;
  private int pointsInLevel;
  private int pointsOverAll;
  private int level;
  private int coins;

  @Relationship(value = "IS")
  private List<ContributorEntity> contributors;

  @Relationship(value = "HAS")
  private List<RefreshTokenEntity> refreshTokens;

  @Relationship(type = "ASSIGNED_TO")
  private List<ProjectEntity> projects;
}
