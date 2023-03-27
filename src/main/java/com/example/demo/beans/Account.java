package com.example.demo.beans;

import com.example.demo.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccount;
    private String login;
    private  String password;
    private RoleType role;
    private Date dateOuverture = new Date();
    private int userRefID;
    //Will be used to enable or disable an account rather than deleting it
   // private boolean enabled = true;
    //TODO FOR ADMIN - COMMERCIAL : EMAIL, ACCESSCODE, type (admin ou non )
}
