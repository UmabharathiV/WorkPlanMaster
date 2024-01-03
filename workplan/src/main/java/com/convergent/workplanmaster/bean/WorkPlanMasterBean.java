package com.convergent.workplanmaster.bean;

//import com.convergent.workplanmaster.validation.PlanDateAndTime;
//import com.convergent.workplanmaster.validation.PlanUniqueName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlanMasterBean {

    private Long id;

    private Long srId;

    @NotNull(message="plan name is required")
    private String planName;

    @NotNull(message = "Plan Start Date is required")
    private Date planStart;

    @NotNull(message = "Plan End Date is required")
    private Date planEnd;

    private Integer daysRemaining;

    private Integer percentageCompleted;

    private Integer delayDays;

    private Date createdAt;

    private Date updatedAt;

    private String status;

    private List<WorkPlanMilestonesBean> listOfWorkPlanMilestones;


}
