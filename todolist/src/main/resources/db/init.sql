-- 数据库初始化脚本
-- TodoList Web应用

CREATE DATABASE IF NOT EXISTS todolist DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE todolist;

-- 用户表
CREATE TABLE t_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色',
    enabled TINYINT(1) DEFAULT 1 COMMENT '启用状态',
    deleted TINYINT(1) DEFAULT 0 COMMENT '删除标记',
    createtime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createuser BIGINT COMMENT '创建人的id',
    updateuser BIGINT COMMENT '更新人的id',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分组表
CREATE TABLE t_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '分组名称',
    color VARCHAR(10) DEFAULT '#409EFF' COMMENT '分组颜色',
    deleted TINYINT(1) DEFAULT 0 COMMENT '删除标记',
    createtime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createuser BIGINT COMMENT '创建人的id',
    updateuser BIGINT COMMENT '更新人的id',
    INDEX idx_t_groups_userid (userid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分组表';

-- 任务表
CREATE TABLE t_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userid BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(200) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    groupid BIGINT COMMENT '分组ID',
    priority TINYINT DEFAULT 2 COMMENT '优先级：1低/2中/3高/4紧急',
    status TINYINT DEFAULT 1 COMMENT '状态：1待办/2进行中/3已完成/4搁置',
    duedate DATETIME COMMENT '截止日期',
    completedtime DATETIME COMMENT '完成时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '删除标记',
    createtime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    createuser BIGINT COMMENT '创建人的id',
    updateuser BIGINT COMMENT '更新人的id',
    INDEX idx_t_tasks_userid (userid),
    INDEX idx_t_tasks_status (status),
    INDEX idx_t_tasks_groupid (groupid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 初始化管理员账号
INSERT INTO t_users (username, password, nickname, role, enabled, deleted, createtime, updatetime)
VALUES ('admin', '$2a$10$2WenNzy3S2nAG0N9B01YMuGMqEi4aZfV48B2PZhHZf1R19o4EQODq', '管理员', 'admin', 1, 0, NOW(), NOW());

-- 初始化默认分组
INSERT INTO t_groups (userid, name, color, deleted, createtime, updatetime)
VALUES (NULL, '默认', '#409EFF', 0, NOW(), NOW());

UPDATE t_users SET password = '$2a$10$2WenNzy3S2nAG0N9B01YMuGMqEi4aZfV48B2PZhHZf1R19o4EQODq' WHERE username = 'admin';

-- 注意：管理员默认密码为 admin123，上面的密码是BCrypt加密后的值
-- 实际部署时请修改密码