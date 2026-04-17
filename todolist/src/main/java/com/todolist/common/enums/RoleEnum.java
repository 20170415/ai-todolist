package com.todolist.common.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMIN("admin", "管理员"),
    USER("user", "普通用户");

    private final String code;
    private final String desc;

    RoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}