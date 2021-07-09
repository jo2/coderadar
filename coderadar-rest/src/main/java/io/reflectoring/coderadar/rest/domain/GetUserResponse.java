package io.reflectoring.coderadar.rest.domain;

import io.reflectoring.coderadar.domain.Badge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
  private long id;
  private String username;
  private boolean platformAdmin = false;

  private int level;
  private int coins;
  private int pointsInLevel;
  private int pointsOverAll;
  private String title;
  private String primaryColor;
  private String secondaryColor;
  private List<Badge> badges;
  private List<Badge> publicBadges;
  private List<Long> solvedQuestIds;
}
