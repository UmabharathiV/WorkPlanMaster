package com.convergent.workplanmaster.repository;

import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMilestonesEntity;
import com.convergent.workplanmaster.entity.WorkPlanTasksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkPlanTasksRepository extends JpaRepository<WorkPlanTasksEntity,Long>{

    Optional<WorkPlanTasksEntity> findById(Long taskId);

    List<WorkPlanTasksEntity> findByWpMilestoneId(WorkPlanMilestonesEntity wpMilestoneId);
}
