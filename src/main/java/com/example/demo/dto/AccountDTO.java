package com.example.demo.dto;

import com.example.demo.beans.Customer;
import com.example.demo.beans.User;
import com.example.demo.enums.RoleType;

import java.util.Date;

public record AccountDTO(
        Integer id,
        String username,
        RoleType role,
        User user,
        Customer customer,
        Date dateOuverture

) {
}
