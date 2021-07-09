package io.reflectoring.coderadar.domain;

import java.util.List;
import lombok.Data;

/** User of the application. Has to login to access to functionality */
@Data
public class User {
  private long id;
  private String username;
  private String password;
  private boolean platformAdmin = false;
  private List<Contributor> contributors;

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
