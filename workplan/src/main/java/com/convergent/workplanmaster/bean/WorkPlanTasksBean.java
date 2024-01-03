package com.convergent.workplanmaster.bean;

import com.convergent.workplanmaster.entity.UserMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMilestonesEntity;
//import com.convergent.workplanmaster.validation.AssignUser;
//import com.convergent.workplanmaster.validation.PlanDateAndTime;
//import com.convergent.workplanmaster.validation.PlanUniqueName;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlanTasksBean {


    private Long id;

    private Long wpMilestoneId;

    private String taskDetail;

    private Long assignedTo;

    private Integer sequence;

    @NotNull(message="plan start should not be null")
    private Date planStart;

    @NotNull(message="plan end should not be null")
    private Date planEnd;

    private Integer plannedDays;

    private Double duration;

    private Date actualStart;

    private Date actualEnd;

    private Integer actualDays;

    private Double actualDuration;

    private Integer currentWorkStatus;

    private Integer completionPercent;

    private Boolean delayedStart;

    private Boolean delayedEnd;

    private Integer planActualDiff;

    private Date createdAt;

    private Long version;

    private String status;

}
