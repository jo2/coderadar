package io.reflectoring.coderadar.analyzer.sonarscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Component {
    private String key;
    private String name;
    private String qualifier;
    private String path;
    private String language;
    private List<Measure> measures;
}
