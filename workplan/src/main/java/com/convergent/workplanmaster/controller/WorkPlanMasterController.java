package com.convergent.workplanmaster.controller;

import com.convergent.workplanmaster.bean.WorkPlanMasterBean;
import com.convergent.workplanmaster.bean.WorkPlanStatusBean;
import com.convergent.workplanmaster.service.WorkPlanMasterService;
//import com.convergent.workplanmaster.service.WorkPlanMilestonesService;
//import com.convergent.workplanmaster.service.WorkPlanTasksService;
import jakarta.validation.Valid;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/workplanmaster")
public class WorkPlanMasterController {

    @Autowired
    private WorkPlanMasterService workPlanMasterService;

    @PostMapping
    public ResponseEntity<String> PlanSave(@Valid @RequestBody WorkPlanMasterBean workPlanMasterBean) {

        try {
                WorkPlanMasterBean savedMaster = workPlanMasterService.createPlan(workPlanMasterBean);

                return ResponseEntity.ok("Master should be created successfully");

           } catch (Exception e) {

               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PutMapping
    public WorkPlanMasterBean updatePlan(@RequestBody WorkPlanMasterBean workPlanMasterBean) {

        return workPlanMasterService.updatePlan(workPlanMasterBean);

    }


    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<String>deleteTaskById(@PathVariable @Valid Long taskId) {
        try {

            workPlanMasterService.deleteTaskById(taskId);

            return ResponseEntity.ok("Task deleted successfully");

            }catch (RuntimeException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

            } catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");

            }
    }


    @DeleteMapping("/{id}")
    public void deleteMilestoneById(@PathVariable Long id) {

            workPlanMasterService.deleteMilestoneById(id);
    }



    @PostMapping("/user/{userId}")
    public ResponseEntity<String>startAndStopTask(@PathVariable String userId, @RequestBody @Valid WorkPlanStatusBean workPlanStatusBean){
        try{
            // Call the service to update the task status
            workPlanMasterService.startAndStopTask(userId,workPlanStatusBean);

            return ResponseEntity.ok("Task status updated successfully");

           } catch (RuntimeException  e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

           }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");

           }
    }

   }








