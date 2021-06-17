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
  private int pointsInLevel;
  private int pointsOverAll;
  private int level;
  private int coins;
}
