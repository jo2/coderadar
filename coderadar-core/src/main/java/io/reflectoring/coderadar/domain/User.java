package io.reflectoring.coderadar.domain;

import lombok.Data;

import java.util.List;

/** User of the application. Has to login to access to functionality */
@Data
public class User {
  private long id;
  private String username;
  private String password;
  private boolean platformAdmin = false;
  private List<Contributor> contributors;
}
