package com.convergent.workplanmaster.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkPlanStatusEnum {

    YET_TO_START(1, "Yet to Start"),

    IN_PROGRESS(2, "In Progress"),

    COMPLETED(3, "Completed");

    private final int id;

    private final String statusType;
}
