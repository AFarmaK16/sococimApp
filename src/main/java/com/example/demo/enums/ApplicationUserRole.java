package com.example.demo.enums;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.enums.ApplicationUserPermission.*;

@Getter @RequiredArgsConstructor
public enum ApplicationUserRole {

    ADV(Sets.newHashSet(MANAGE_CUSTOMERS, MANAGE_ORDERS,UPDATE_ORDERS, LIST_CUSTOMERS,LIST_ORDERS)),

    CUSTOMER(Sets.newHashSet(ORDER_PRODUCT,LIST_PRODUCTS,ADD_CUSTOMER)), //for giving a set of permissions to a role
    COMMERCIAL(Sets.newHashSet(MANAGE_PRODUCTS,LIST_ORDERS,SETTING_ORDERS_PARAMS)), //meaning this user do not have yet permissions
    ADMIN(Sets.newHashSet(MANAGE_USERS))
    ;
    private final Set<ApplicationUserPermission> permissionSet;

//    ApplicationUserRole(Set<ApplicationUserPermission> permissionSet) {
//        this.permissionSet = permissionSet;
//    }
    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> permissionSet = getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        permissionSet.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissionSet;
    }
}
