package com.convergent.workplanmaster.bean;

import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
//import com.convergent.workplanmaster.validation.PlanDateAndTime;
//import com.convergent.workplanmaster.validation.PlanUniqueName;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class WorkPlanMilestonesBean {

    private Long id;

    private Long wpMasterId;

    @NotNull(message = "Milestone must not be null ")
    private String milestonesName;

    @NotNull(message="plan start is required")
    private Date planStart;

    @NotNull(message="plan End is required")
    private Date planEnd;

    private Integer sequence;

    private Integer plannedDays;

    private Double duration;

    private Date actualStart;

    private Date actualEnd;

    private Integer actualDays;

    private Double actualDuration;

    private Integer currentWorkStatus;

    private Integer completionPercent;

    private Integer planActualDiff;

    private Date createdAt;

    private Date updatedAt;

    private Long version;

    private Boolean status;


    private List<WorkPlanTasksBean> listOfWorkPlanTasks;
}


