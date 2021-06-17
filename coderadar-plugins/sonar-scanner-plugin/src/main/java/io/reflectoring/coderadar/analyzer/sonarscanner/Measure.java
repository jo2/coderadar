package io.reflectoring.coderadar.analyzer.sonarscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measure {
  private String metric;
  private Double value;
  private Boolean bestValue;
}
