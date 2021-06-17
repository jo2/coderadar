package io.reflectoring.coderadar.domain;

import lombok.Data;

@Data
public class ProjectResponse {
  private String key;
  private String name;
  private String qualifier;
}
