package com.convergent.workplanmaster.service;

import com.convergent.workplanmaster.bean.WorkPlanMasterBean;
import com.convergent.workplanmaster.bean.WorkPlanStatusBean;

import java.util.List;

public interface WorkPlanMasterService {

      WorkPlanMasterBean createPlan(WorkPlanMasterBean workPlanMasterBean);

      WorkPlanMasterBean updatePlan(WorkPlanMasterBean workPlanMasterBean);

      void deleteTaskById(Long id);

      void deleteMilestoneById(Long id);

      void startAndStopTask(String userId, WorkPlanStatusBean workPlanStatusBean);
}
