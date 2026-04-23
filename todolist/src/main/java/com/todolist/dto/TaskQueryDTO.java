package com.todolist.dto;

import lombok.Data;

@Data
public class TaskQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private Integer status;
    private Long groupid;
    private Integer priority;
}