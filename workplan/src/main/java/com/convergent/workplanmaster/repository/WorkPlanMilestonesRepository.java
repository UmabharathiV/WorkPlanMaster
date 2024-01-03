package com.convergent.workplanmaster.repository;

import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMilestonesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkPlanMilestonesRepository extends JpaRepository<WorkPlanMilestonesEntity,Long> {
    boolean existsBymilestonesName(String milestonesName);

    Optional<WorkPlanMilestonesEntity>findByid(Long id);


    List<WorkPlanMilestonesEntity> findByWpMasterId(WorkPlanMasterEntity wpMasterId);
}
