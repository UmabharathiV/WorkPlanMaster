package com.convergent.workplanmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="work_plan_tasks")
public class WorkPlanTasksEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wpMilestoneId", referencedColumnName = "id")
    @JsonIgnore
    private WorkPlanMilestonesEntity wpMilestoneId;

    @Column(name="task_detail")
    private String taskDetail;

    @Column(name="assigned_to")
    private Long assignedTo;

    @Column(name="sequenceNum")
    private Integer sequence;

    @Column(name="planning_start")
    private Date planStart;

    @Column(name="planning_end")
    private Date planEnd;

    @Column(name="planned_days")
    private Integer plannedDays;

    @Column(name="duration_time")
    private Double duration;

    @Column(name="dependent_id")
    private Integer dependentId;

    @Column(name="actual_start")
    private Date actualStart;

    @Column(name="actual_end")
    private Date actualEnd;

    @Column(name="actual_days")
    private Integer actualDays;

    @Column(name="actual_duration")
    private Double actualDuration;

    @Column(name="currentwork_status")
    private Integer currentWorkStatus;

    @Column(name="percentage_completion")
    private Integer completionPercent;

    @Column(name="remarks")
    private String remarks;

    @Column(name="delayed_start")
    private Boolean delayedStart;

    @Column(name="delayed_end")
    private Boolean delayedEnd;

    @Column(name="plan_actualdiff")
    private Integer planActualDiff;

    @Column(name="created_at")
    private Long createdAt;

    @Column(name="version")
    private Long version;

    @Column(name="status")
    private  String status;



}
