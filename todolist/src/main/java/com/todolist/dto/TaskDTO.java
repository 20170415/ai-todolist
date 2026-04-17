package com.todolist.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    private Long groupid;

    private Integer priority;

    private Integer status;

    private LocalDateTime duedate;

}