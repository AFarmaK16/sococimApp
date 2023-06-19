package com.example.demo.enums;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.enums.ApplicationUserPermission.*;

@Getter
public enum ApplicationUserRole {

    ROLE_ADMIN(Sets.newHashSet(MANAGE_USERS)),
    ROLE_ADV(Sets.newHashSet(MANAGE_CUSTOMERS, MANAGE_ORDERS,UPDATE_ORDERS, LIST_CUSTOMERS,LIST_ORDERS)),
    ROLE_COMM(Sets.newHashSet(MANAGE_PRODUCTS,LIST_ORDERS,SETTING_ORDERS_PARAMS)), //meaning this user do not have yet permissions
    ROLE_CUSTOMER(Sets.newHashSet(ORDER_PRODUCT,LIST_PRODUCTS,ADD_CUSTOMER)) //for giving a set of permissions to a role
    ;
    private final Set<ApplicationUserPermission> permissionSet;

    ApplicationUserRole(Set<ApplicationUserPermission> permissionSet) {
        this.permissionSet = permissionSet;
    }
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissionSet = getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissionSet.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissionSet;
    }
}
