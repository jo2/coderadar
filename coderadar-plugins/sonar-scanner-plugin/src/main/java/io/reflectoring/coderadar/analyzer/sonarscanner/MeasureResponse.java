package io.reflectoring.coderadar.analyzer.sonarscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasureResponse {
    private Paging paging;
    private BaseComponent baseComponent;
    private List<Component> components;
}
