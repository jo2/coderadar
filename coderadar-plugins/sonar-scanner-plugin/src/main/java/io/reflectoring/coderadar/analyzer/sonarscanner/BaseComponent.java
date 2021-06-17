package io.reflectoring.coderadar.analyzer.sonarscanner;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseComponent {
  private String key;
  private String name;
  private String qualifier;
  private List<Measure> measures;
}
