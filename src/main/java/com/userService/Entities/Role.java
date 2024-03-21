package com.userService.Entities;



import com.userService.Enum.RoleType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name="User_Role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ROLE_SEQ")
    @SequenceGenerator(name = "ROLE_SEQ",allocationSize = 1)
    @Column(name = "ROLE_ID")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleType name;

    @Column
    private String description;

    @Column
    private char status;

    @Column
    private Date createdDate;

    @Column
    private Date updatedDate;

}
