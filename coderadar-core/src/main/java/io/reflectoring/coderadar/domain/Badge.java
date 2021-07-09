package io.reflectoring.coderadar.domain;

import lombok.Data;

@Data
public class Badge {
    private Long id;
    private String title;
    private int amountNeeded;
    private int currentAmount;
    private String metric;
}
