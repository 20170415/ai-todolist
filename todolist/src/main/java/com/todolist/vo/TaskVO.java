package com.todolist.vo;

import com.todolist.common.enums.PriorityEnum;
import com.todolist.common.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    private Long id;

    private String title;

    private String description;

    private Long groupid;

    private String groupname;

    private String groupcolor;

    private Integer priority;

    private String priorityDesc;

    private Integer status;

    private String statusDesc;

    private LocalDateTime duedate;

    private LocalDateTime completedtime;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    private String createUserNickname;

    public void setPriority(Integer priority) {
        this.priority = priority;
        PriorityEnum priorityEnum = PriorityEnum.getByCode(priority);
        if (priorityEnum != null) {
            this.priorityDesc = priorityEnum.getDesc();
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
        StatusEnum statusEnum = StatusEnum.getByCode(status);
        if (statusEnum != null) {
            this.statusDesc = statusEnum.getDesc();
        }
    }

}