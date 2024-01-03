package com.convergent.workplanmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="work_plan_milestones")
public class WorkPlanMilestonesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wpMasterId", referencedColumnName = "id")
    @JsonIgnore
    private WorkPlanMasterEntity wpMasterId;

    @Column(name="milestones_name")
    private String milestonesName;

    @Column(name="planning_start")
    private Date planStart;

    @Column(name="planning_end")
    private Date planEnd;

    @Column(name="sequence")
    private Integer sequence;

    @Column(name="planned_days")
    private Integer plannedDays;

    @Column(name="duration_time")
    private Double duration;

    @Column(name="actual_start")
    private Date actualStart;

    @Column(name="actual_end")
    private Date actualEnd;

    @Column(name="actual_days")
    private Integer actualDays;

    @Column(name="actual_duration")
    private Double actualDuration;

    @Column(name="currentwork_status")
    private Integer currentworkStatus;

    @Column(name="completion_percentage")
    private Integer completionPercent;

    @Column(name="remarks")
    private String remarks;

    @Column(name="plan_actualdiff")
    private Integer planActualDiff;

    private Date createdAt;

    private Date updatedAt;

    private Boolean status;

    private Long version;

    @OneToMany(mappedBy="wpMilestoneId",cascade=CascadeType.ALL)
    private List<WorkPlanTasksEntity> listOfWorkPlanTasks;



}
