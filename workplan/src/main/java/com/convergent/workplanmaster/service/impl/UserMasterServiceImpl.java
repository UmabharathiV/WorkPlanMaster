
package com.convergent.workplanmaster.service.impl;

import com.convergent.workplanmaster.bean.UserMasterBean;
import com.convergent.workplanmaster.bean.WorkPlanMasterBean;
import com.convergent.workplanmaster.entity.UserMasterEntity;
import com.convergent.workplanmaster.entity.WorkPlanMasterEntity;
import com.convergent.workplanmaster.repository.UserMasterRepository;
import com.convergent.workplanmaster.service.UserMasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMasterServiceImpl implements UserMasterService {
    @Autowired
    private UserMasterRepository userMasterRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public UserMasterBean createUser(UserMasterBean userMasterBean) {

              UserMasterEntity userMasterEntity = modelMapper.map(userMasterBean, UserMasterEntity.class);

              UserMasterEntity savedUserMasterEntity = userMasterRepository.save(userMasterEntity);

              UserMasterBean savedUserMasterBean = modelMapper.map(savedUserMasterEntity, UserMasterBean.class);

              return savedUserMasterBean;

        }
    }


