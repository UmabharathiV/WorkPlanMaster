
package com.convergent.workplanmaster.controller;

import com.convergent.workplanmaster.bean.UserMasterBean;
import com.convergent.workplanmaster.service.UserMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usermaster")
public class UserMasterController {
    @Autowired
    UserMasterService userMasterService;

    @PostMapping
    public UserMasterBean createUser(@RequestBody UserMasterBean userMasterBean) {

        return userMasterService.createUser(userMasterBean);
    }
}

