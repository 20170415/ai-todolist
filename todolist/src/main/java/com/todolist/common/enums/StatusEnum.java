package com.todolist.common.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    TODO(1, "待办"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "已完成"),
    SHELVED(4, "搁置");

    private final Integer code;
    private final String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StatusEnum getByCode(Integer code) {
        for (StatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

}