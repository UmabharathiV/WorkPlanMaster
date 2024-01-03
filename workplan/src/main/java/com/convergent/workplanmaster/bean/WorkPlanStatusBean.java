package com.convergent.workplanmaster.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlanStatusBean {

    private WorkPlanStatusEnum status;

    private Date statusChangeDate;

    private Long taskId;
}
