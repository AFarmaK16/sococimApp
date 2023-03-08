package com.example.demo.enums;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.enums.ApplicationUserPermission.*;

@Getter
public enum ApplicationUserRole {

    admin(Sets.newHashSet(MANAGE_USERS, MANAGE_ORDERS, LIST_USERS,MANAGE_PRODUCTS,LIST_CUSTOMERS,LIST_PRODUCTS)),
    customer(Sets.newHashSet(ORDER_PRODUCT,LIST_PRODUCTS,ADD_CUSTOMER)) //for giving a set of permissions to a role
    ,
    deliver(Sets.newHashSet()) //meaning this user do not have yet permissions
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
