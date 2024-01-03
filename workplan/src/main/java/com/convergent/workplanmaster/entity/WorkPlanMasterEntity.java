package com.convergent.workplanmaster.entity;

import com.convergent.workplanmaster.bean.WorkPlanTasksBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="work_plan_master")
public class WorkPlanMasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serial_id")
    private Long srId;

    @Column(name = "plan_name")//unique = true)
    private String planName;

    @Column(name = "planning_start")
    private Date planStart;

    @Column(name = "planning_end")
    private Date planEnd;

    @Column(name = "remaining_days")
    private Integer daysRemaining;

    @Column(name = "percentage_completed")
    private Integer percentageCompleted;

    @Column(name = "delayed_days")
    private Integer delayDays;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    private String status;

    @OneToMany(mappedBy = "wpMasterId", cascade = CascadeType.ALL)
    private List<WorkPlanMilestonesEntity> listOfWorkPlanMilestones;

    }




