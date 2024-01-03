package com.convergent.workplanmaster.repository;

import com.convergent.workplanmaster.entity.UserMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMasterEntity,Long> {
         Optional<UserMasterEntity>findById(Long assignedTo);

}
