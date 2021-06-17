package io.reflectoring.coderadar.analyzer.sonarscanner;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasureResponse {
  private Paging paging;
  private BaseComponent baseComponent;
  private List<Component> components;
}
