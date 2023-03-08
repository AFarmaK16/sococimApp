package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum ApplicationUserPermission {

    LIST_USERS("users:read"), MANAGE_USERS("users: manage"),

    MANAGE_ORDERS("orders:manage"), LIST_ORDERS("orders:read"),
    ORDER_PRODUCT("order:create"), LIST_PRODUCTS("products:list"),MANAGE_PRODUCTS("products:manage"),
    LIST_CUSTOMERS("customer:read"),ADD_CUSTOMER("customer: create");

private final String permission;


    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
