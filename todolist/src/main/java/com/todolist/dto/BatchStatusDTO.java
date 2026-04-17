package com.todolist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchStatusDTO {

    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    @NotNull(message = "状态不能为空")
    private Integer status;

}