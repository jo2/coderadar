package io.reflectoring.coderadar.sonar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
  private String key;
  private String name;
  private String qualifier;
  private String visibility;
}
