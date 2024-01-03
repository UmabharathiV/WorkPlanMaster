package com.convergent.workplanmaster.service.impl;

import com.convergent.workplanmaster.bean.*;
import com.convergent.workplanmaster.entity.UserMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMilestonesEntity;
import com.convergent.workplanmaster.entity.WorkPlanTasksEntity;
import com.convergent.workplanmaster.repository.UserMasterRepository;
import com.convergent.workplanmaster.repository.WorkPlanMasterRepository;
import com.convergent.workplanmaster.repository.WorkPlanMilestonesRepository;
import com.convergent.workplanmaster.repository.WorkPlanTasksRepository;
import com.convergent.workplanmaster.service.WorkPlanMasterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkPlanMasterServiceImpl implements WorkPlanMasterService {

    @Autowired
    private WorkPlanMasterRepository workPlanMasterRepository;
    @Autowired
    private WorkPlanMilestonesRepository workPlanMilestonesRepository;
    @Autowired
    private WorkPlanTasksRepository workPlanTasksRepository;
    @Autowired
    private UserMasterRepository userMasterRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WorkPlanMasterBean createPlan(WorkPlanMasterBean workPlanMasterBean) {

        if (workPlanMasterBean.getPlanStart().after(workPlanMasterBean.getPlanEnd())) {

            throw new IllegalArgumentException("Master plan start date must be before end date");
        }

        if (workPlanMasterRepository.existsByPlanName(workPlanMasterBean.getPlanName())) {

            throw new IllegalArgumentException("Workplan name is unique");
        }

        WorkPlanMasterEntity workPlanMasterEntity = modelMapper.map(workPlanMasterBean, WorkPlanMasterEntity.class);

        List<WorkPlanMilestonesBean> listOfMilestones = workPlanMasterBean.getListOfWorkPlanMilestones();

        List<WorkPlanMilestonesEntity> listOfMilestonesEntities = new ArrayList<>();

        if (listOfMilestones != null) {

            Set<String> milestoneNames = new HashSet<>();

            for (WorkPlanMilestonesBean milestonesBean : listOfMilestones) {

                String milestoneName = milestonesBean.getMilestonesName();

                if (!milestoneNames.add(milestonesBean.getMilestonesName())) {

                    throw new IllegalArgumentException("Milestone name is unique");
                }
                WorkPlanMilestonesEntity milestonesEntity = modelMapper.map(milestonesBean, WorkPlanMilestonesEntity.class);

                milestonesEntity.setWpMasterId(workPlanMasterEntity);

                    if (milestonesEntity.getPlanStart().after(milestonesEntity.getPlanEnd())) {

                    throw new IllegalArgumentException("Milestone plan start date must be before end date");
                    }

                plannedDaysCalculatedOnMilestone(milestonesEntity);

                calculateDurationOnMilestone(milestonesEntity);

                List<WorkPlanTasksBean> listOfTasks = milestonesBean.getListOfWorkPlanTasks();

                List<WorkPlanTasksEntity> listOfTasksEntities = new ArrayList<>();

                     if (listOfTasks != null) {

                        for (WorkPlanTasksBean tasksBean : listOfTasks) {

                        WorkPlanTasksEntity tasksEntity = modelMapper.map(tasksBean, WorkPlanTasksEntity.class);

                        tasksEntity.setWpMilestoneId(milestonesEntity);

                        tasksEntity.setStatus(WorkPlanStatusEnum.YET_TO_START.getStatusType());

                        Long userMasterId = tasksBean.getAssignedTo();

                        Optional<UserMasterEntity> userMaster = userMasterRepository.findById(userMasterId);

                            if (userMaster.isPresent()) {

                            tasksEntity.setAssignedTo(userMasterId);

                             } else {

                            throw new NoSuchElementException("User not found");
                        }

                        plannedDaysCalculatedOnTask(tasksEntity);

                        calculateDurationOnTask(tasksEntity);

                        listOfTasksEntities.add(tasksEntity);

                    }

                }

                milestonesEntity.setListOfWorkPlanTasks(listOfTasksEntities);

                calculateTasksSequenceBasedOnStartDate(milestonesEntity.getListOfWorkPlanTasks());

                calculateMilestoneSequenceBasedOnStartDate(listOfMilestonesEntities);

                calculateActualDaysOnMilestone(milestonesEntity);

                listOfMilestonesEntities.add(milestonesEntity);

            }

        }
        workPlanMasterEntity.setListOfWorkPlanMilestones(listOfMilestonesEntities);

        daysRemainingCalculatedOnMaster(workPlanMasterEntity);

        workPlanMasterEntity.setStatus(WorkPlanStatusEnum.YET_TO_START.getStatusType());

        WorkPlanMasterEntity savedWorkPlanMasterEntity = workPlanMasterRepository.save(workPlanMasterEntity);

        return modelMapper.map(savedWorkPlanMasterEntity, WorkPlanMasterBean.class);
    }


    @Override
    public WorkPlanMasterBean updatePlan(WorkPlanMasterBean updatedWorkPlanMasterBean) {

        Long workPlanMasterId = updatedWorkPlanMasterBean.getId();

        WorkPlanMasterEntity workPlanMasterEntity = workPlanMasterRepository.findById(workPlanMasterId).orElse(null);

        if (workPlanMasterEntity == null)
            return null;

        List<WorkPlanMilestonesEntity> listOfWorkPlanMilestones = workPlanMasterEntity.getListOfWorkPlanMilestones();

        List<WorkPlanMilestonesBean> listOfWorkPlanMilestones1 = updatedWorkPlanMasterBean.getListOfWorkPlanMilestones();

        int count = 0;

        if (listOfWorkPlanMilestones != null)
        {
            for (WorkPlanMilestonesEntity listOfWorkPlanMilestone : listOfWorkPlanMilestones)
            {
                count = listOfWorkPlanMilestone.getListOfWorkPlanTasks().size();

                Long milestoneIdEntity = listOfWorkPlanMilestone.getId();

                List<WorkPlanTasksEntity> listOfTaskEntity = new ArrayList<>();

                for (WorkPlanMilestonesBean workPlanMilestonesBean : listOfWorkPlanMilestones1) {

                    Long milestoneId = workPlanMilestonesBean.getId();

                    if (milestoneIdEntity == milestoneId)
                    {
                        //List<WorkPlanTasksBean> collect = workPlanMilestonesBean.getListOfWorkPlanTasks().stream().filter(map -> map.getId() == null).collect(Collectors.toList());

                        List<WorkPlanTasksBean> collect = new ArrayList<>();

                        for (WorkPlanTasksBean listOfWorkPlanTask : workPlanMilestonesBean.getListOfWorkPlanTasks())
                        {
                           if ( listOfWorkPlanTask.getId() == null )
                           {
                               collect.add(listOfWorkPlanTask);
                           }
                        }

                        for (WorkPlanTasksBean workPlanTasksBean : collect)
                        {
                            workPlanTasksBean.setStatus(WorkPlanStatusEnum.YET_TO_START.getStatusType());
                            count = count+1;
                            workPlanTasksBean.setSequence(count);

                            workPlanTasksBean.setWpMilestoneId(milestoneIdEntity);

                            listOfTaskEntity.add(modelMapper.map(workPlanTasksBean,WorkPlanTasksEntity.class));
                        }
                    }
                }
                listOfWorkPlanMilestone.getListOfWorkPlanTasks().addAll(listOfTaskEntity);//no need set
            }
        }

        WorkPlanMasterEntity save = workPlanMasterRepository.save(workPlanMasterEntity);

        return modelMapper.map(save, WorkPlanMasterBean.class);
    }



    @Override
    public void deleteTaskById(Long id) {

        Optional<WorkPlanTasksEntity> taskOptional = workPlanTasksRepository.findById(id);

        if (taskOptional.isPresent()) {

            WorkPlanTasksEntity taskEntity = taskOptional.get();

            if (taskEntity.getStatus() == null || taskEntity.getStatus().equals(WorkPlanStatusEnum.YET_TO_START.getStatusType())) {

                workPlanTasksRepository.delete(taskEntity);

                updateSequenceForTasks(taskEntity);

            } else if (taskEntity.getStatus().equals(WorkPlanStatusEnum.IN_PROGRESS.getStatusType()) ||
                    taskEntity.getStatus().equals(WorkPlanStatusEnum.COMPLETED.getStatusType())) {

                throw new RuntimeException("Task should not be deleted");
            }
        }
    }

    @Override
    public void deleteMilestoneById(Long id) {
        try {
            Optional<WorkPlanMilestonesEntity> milestonesOptional = workPlanMilestonesRepository.findByid(id);

            if (milestonesOptional.isPresent()) {

                WorkPlanMilestonesEntity planMilestonesEntity = milestonesOptional.get();

                workPlanMilestonesRepository.delete(planMilestonesEntity);

                updateSequenceForMilestone(planMilestonesEntity);
            }

            } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void calculateTasksSequenceBasedOnStartDate(List<WorkPlanTasksEntity> workPlanTasksEntities) {

      /* List<WorkPlanTasksEntity> planTasksEntities=new ArrayList<>();
       planTasksEntities.addAll(workPlanTasksEntities);*/

       Comparator<WorkPlanTasksEntity> tasksEntityComparator = Comparator.comparing(WorkPlanTasksEntity::getPlanStart);

       workPlanTasksEntities.sort(tasksEntityComparator);

        int count = 1;

        for (WorkPlanTasksEntity task : workPlanTasksEntities) {

            task.setSequence(count);

            count++;
        }

        }



    private void calculateMilestoneSequenceBasedOnStartDate(List<WorkPlanMilestonesEntity> workPlanMilestonesEntities) {

       /*List<WorkPlanTasksEntity> planTasksEntities=new ArrayList<>();
       planTasksEntities.addAll(workPlanTasksEntities);*/

        Comparator<WorkPlanMilestonesEntity> planMilestonesEntityComparator = Comparator.comparing(WorkPlanMilestonesEntity::getPlanStart);

        workPlanMilestonesEntities.sort(planMilestonesEntityComparator);

        int count = 1;

        for (WorkPlanMilestonesEntity planMilestones : workPlanMilestonesEntities) {

            planMilestones.setSequence(count);

            count++;
        }

    }



    @Override
    public void startAndStopTask(String userId, WorkPlanStatusBean workPlanStatusBean) {

        Optional<WorkPlanTasksEntity> optionalPlanTasksEntity = workPlanTasksRepository.findById(workPlanStatusBean.getTaskId());

        if (optionalPlanTasksEntity.isPresent()) {

            WorkPlanTasksEntity planTasksEntity = optionalPlanTasksEntity.get();

            Long parsedUserId = Long.parseLong(userId);

            // Check if the user ID matches the assigned user for the task
            if (!planTasksEntity.getAssignedTo().equals(parsedUserId)) {

                throw new RuntimeException("Not authorized to update the tasks");
            }
            // Get the status from the workPlanStatusBean and handle tasks based on the status
               if (workPlanStatusBean.getStatus() == WorkPlanStatusEnum.IN_PROGRESS) {

                   planTasksEntity.setActualStart(workPlanStatusBean.getStatusChangeDate());

                   planTasksEntity.setStatus(workPlanStatusBean.getStatus().getStatusType());

               } else if (workPlanStatusBean.getStatus() == WorkPlanStatusEnum.COMPLETED) {

                    if (workPlanStatusBean.getStatusChangeDate() != null) {

                       planTasksEntity.setActualEnd(workPlanStatusBean.getStatusChangeDate());

                        Date actualStart = planTasksEntity.getActualStart();

                        Date planStart = planTasksEntity.getPlanStart();

                        planTasksEntity.setDelayedStart(actualStart != null && planStart != null && actualStart.after(planStart));

                        Date actualEnd = planTasksEntity.getActualEnd();

                        Date planEnd = planTasksEntity.getPlanEnd();

                        planTasksEntity.setDelayedEnd(actualEnd != null && planEnd != null && actualEnd.after(planEnd));

                       planTasksEntity.setStatus(workPlanStatusBean.getStatus().getStatusType());

                       calculateActualDaysOnTask(planTasksEntity);

                      planTasksEntity.setCompletionPercent(100);

                      updateMilestonePercentage(planTasksEntity);

                      updateMasterPercentage(planTasksEntity.getWpMilestoneId());
                }
            }
            workPlanTasksRepository.save(planTasksEntity);
        }
    }


    private void updateMilestonePercentage(WorkPlanTasksEntity planTasksEntity) {

        WorkPlanMilestonesEntity mileStoneEntity = planTasksEntity.getWpMilestoneId();

        if (mileStoneEntity != null) {

            List<WorkPlanTasksEntity> workPlanTasksEntities = mileStoneEntity.getListOfWorkPlanTasks();

            int totalTasks = workPlanTasksEntities.size();

            if (totalTasks > 0) {

                int completedTasks = (int) workPlanTasksEntities.stream()
                        .filter(task -> task != null && task.getCompletionPercent() != null)
                        .count();

                int percentageCalculation = (completedTasks * 100) / totalTasks;

                mileStoneEntity.setCompletionPercent(percentageCalculation);

            } else {

                mileStoneEntity.setCompletionPercent(0);

            }

            workPlanMilestonesRepository.save(mileStoneEntity);
        }
    }


    private void updateMasterPercentage(WorkPlanMilestonesEntity wpMilestoneId) {

        WorkPlanMasterEntity workPlanMasterEntity = wpMilestoneId.getWpMasterId(); //getWorkPlanMasterId()

        if (workPlanMasterEntity != null) {

            List<WorkPlanMilestonesEntity> milestoneEntities = workPlanMasterEntity.getListOfWorkPlanMilestones();

            if (milestoneEntities != null && !milestoneEntities.isEmpty()) {
                // Calculate the sum of completion percentages for milestones with non-null completion percentages
//                int milestonePercent = milestoneEntities.stream()
//                        .filter(milestone -> milestone != null && milestone.getCompletionPercent() != null)
//                        .mapToInt(WorkPlanMilestonesEntity::getCompletionPercent)
//                        .sum();

                int milestonePercentage = 0;

                for (WorkPlanMilestonesEntity milestoneEntity : milestoneEntities)
                {
                    if (milestoneEntity != null && milestoneEntity.getCompletionPercent() != null)
                    {
                        milestonePercentage = milestonePercentage + milestoneEntity.getCompletionPercent();
                    }
                }

                // Calculate the average completion percentage for the master plan
                int masterPercent = milestonePercentage / milestoneEntities.size();

                // Set the calculated completion percentage to the master entity
                workPlanMasterEntity.setPercentageCompleted(masterPercent);

            } else {
                // If there are no milestones, set master completion percentage to 0
                workPlanMasterEntity.setPercentageCompleted(0);
            }
            // Save the changes to the database
            workPlanMasterRepository.save(workPlanMasterEntity);
        }
    }


    private void plannedDaysCalculatedOnMilestone(WorkPlanMilestonesEntity workPlanMilestonesEntity) {

        Date planStart = workPlanMilestonesEntity.getPlanStart();

        Date planEnd = workPlanMilestonesEntity.getPlanEnd();

        if (planStart != null && planEnd != null) {

            long dateDiff = planEnd.getTime() - planStart.getTime();

            long diffInDays = TimeUnit.MILLISECONDS.toDays(dateDiff);

            workPlanMilestonesEntity.setPlannedDays((int) diffInDays);

        } else {

            workPlanMilestonesEntity.setPlannedDays(0);
        }
    }


    private void calculateDurationOnMilestone(WorkPlanMilestonesEntity workPlanMilestonesEntity) {

        int plannedDays = workPlanMilestonesEntity.getPlannedDays();

        workPlanMilestonesEntity.setDuration((double) (plannedDays * 8));
    }

    public Integer daysRemainingCalculatedOnMaster(WorkPlanMasterEntity workPlanMasterEntity) {

        Date planStart = workPlanMasterEntity.getPlanStart();

        Date planEnd = workPlanMasterEntity.getPlanEnd();

        if (planStart != null || planEnd != null) {
            //calculate the difference in time between two Date objects in milliseconds.
            long dateDiff = planEnd.getTime() - planStart.getTime();
            //conversion from milliseconds to days.
            long diffInDays = TimeUnit.MILLISECONDS.toDays(dateDiff);

            workPlanMasterEntity.setDaysRemaining((int) diffInDays);

        } else {

            workPlanMasterEntity.setDaysRemaining(0);
        }
        return null;
    }


    private void plannedDaysCalculatedOnTask(WorkPlanTasksEntity workPlanTasksEntity) {

        Date planStart = workPlanTasksEntity.getPlanStart();

        Date planEnd = workPlanTasksEntity.getPlanEnd();

        if (planStart != null && planEnd != null) {

            long dateDiff = planEnd.getTime() - planStart.getTime();

            long diffInDays = TimeUnit.MILLISECONDS.toDays(dateDiff);

            workPlanTasksEntity.setPlannedDays((int) diffInDays);

        } else {

            workPlanTasksEntity.setPlannedDays(0);
        }
    }


    private void calculateDurationOnTask(WorkPlanTasksEntity workPlanTasksEntity) {

        int plannedDays = workPlanTasksEntity.getPlannedDays();

        workPlanTasksEntity.setDuration((double) (plannedDays * 8));
    }

    private void calculateActualDaysOnTask(WorkPlanTasksEntity workPlanTasksEntity) {

        long dateDiff = workPlanTasksEntity.getActualEnd().getTime() - workPlanTasksEntity.getActualStart().getTime();

        long diffInHours = TimeUnit.MILLISECONDS.toHours(dateDiff); // Calculate duration in hours

        long actualDays = diffInHours / 8; // Assuming 8 hours per day

        workPlanTasksEntity.setActualDays((int) actualDays);

        long actualDuration = actualDays * 8; // Assuming 8 hours per day

        workPlanTasksEntity.setActualDuration((double) actualDuration);

        int planActualDiff = workPlanTasksEntity.getActualDays() - workPlanTasksEntity.getPlannedDays();

        workPlanTasksEntity.setPlanActualDiff(planActualDiff);

    }


    private void calculateActualDaysOnMilestone(WorkPlanMilestonesEntity workPlanMilestonesEntity) {

        long dateDiff = workPlanMilestonesEntity.getActualEnd().getTime() -workPlanMilestonesEntity.getActualStart().getTime();

        long diffInHours = TimeUnit.MILLISECONDS.toHours(dateDiff); // Calculate duration in hours

        long actualDays = diffInHours / 8; // Assuming 8 hours per day

       workPlanMilestonesEntity.setActualDays((int) actualDays);

        long actualDuration = actualDays * 8; // Assuming 8 hours per day

        workPlanMilestonesEntity.setActualDuration((double) actualDuration);

        int planActualDiff = workPlanMilestonesEntity.getActualDays() - workPlanMilestonesEntity.getPlannedDays();

        workPlanMilestonesEntity.setPlanActualDiff(planActualDiff);

    }


    private void updateSequenceForTasks(WorkPlanTasksEntity planTasksEntity) {
        List<WorkPlanTasksEntity> updateSequence = workPlanTasksRepository.findByWpMilestoneId(planTasksEntity.getWpMilestoneId());
        // Sort tasks by planStart to maintain correct order
        updateSequence.sort(Comparator.comparing(WorkPlanTasksEntity::getPlanStart));
        // Update sequence for remaining tasks
        int count = 1;

        for (WorkPlanTasksEntity task : updateSequence) {

            if (!task.getId().equals(planTasksEntity.getId())) {

                task.setSequence(count);

                count++;
            }
        }
        workPlanTasksRepository.saveAll(updateSequence);
    }


    private void updateSequenceForMilestone(WorkPlanMilestonesEntity planMilestonesEntity) {
        List<WorkPlanMilestonesEntity> updateSequence = workPlanMilestonesRepository.findByWpMasterId(planMilestonesEntity.getWpMasterId());
        // Sort tasks by planStart to maintain correct order
        updateSequence.sort(Comparator.comparing(WorkPlanMilestonesEntity::getPlanStart));
        // Update sequence for remaining tasks
        int count = 1;

        for (WorkPlanMilestonesEntity planMilestones : updateSequence) {

            if (!planMilestones.getId().equals(planMilestonesEntity.getId())) {

                planMilestones.setSequence(count);

                count++;
            }
        }
        workPlanMilestonesRepository.saveAll(updateSequence);
    }
}


















