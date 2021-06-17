package io.reflectoring.coderadar.analyzer.sonarscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paging {
  private Double pageIndex;
  private Double pageSize;
  private Double total;
}
