package com.example.demo.dto;

import com.example.demo.beans.Customer;
import com.example.demo.beans.User;

import java.util.Date;

public record AccountDTO(
        Integer id,
        String username,
        com.example.demo.enums.ApplicationUserRole role,
        User user,
        Customer customer,
        Date dateOuverture,
        Boolean isEnabled,
        Boolean isAccountNonLocked
) {
}
