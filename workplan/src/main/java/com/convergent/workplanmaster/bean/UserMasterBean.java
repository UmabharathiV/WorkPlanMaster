package com.convergent.workplanmaster.bean;

//import com.convergent.workplanmaster.validation.AssignUser;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
//@AssignUser
public class UserMasterBean {

    private Long id;

    private String userName;

    private String emailId;
}
