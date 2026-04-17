package com.todolist.common.enums;

import lombok.Getter;

@Getter
public enum PriorityEnum {

    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");

    private final Integer code;
    private final String desc;

    PriorityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PriorityEnum getByCode(Integer code) {
        for (PriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return null;
    }

}