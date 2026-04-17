# TodoList Web应用 - 需求文档

## 1. 项目概述

一个多用户的任务管理Web应用，支持管理员管理用户，普通用户管理自己的任务列表。

## 2. 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot + Java + MySQL + Redis |
| 前端 | Vue.js |
| 认证 | JWT Token |

## 3. 用户体系

### 3.1 角色定义
- **管理员(admin)**: 系统内置账号，拥有最高权限
- **普通用户(user)**: 由管理员创建，仅管理自己的任务

### 3.2 登录规则
- 登录页面无注册入口
- 普通用户账号由管理员在后台创建
- 管理员账号初始内置（可配置默认密码）

## 4. 功能模块

### 4.1 任务管理（普通用户）
| 功能 | 描述 |
|------|------|
| 创建任务 | 标题、描述、分组、优先级、截止日期 |
| 编辑任务 | 修改任务各项属性 |
| 删除任务 | 软删除 |
| 任务状态 | 待办/进行中/已完成/搁置 |
| 任务分组 | 支持自定义分组/标签 |
| 优先级 | 紧急/高/中/低 或 数字等级 |
| 截止日期 | 设置due date，逾期任务高亮显示 |
| 列表筛选 | 按状态/分组/优先级/日期筛选 |
| 完成统计 | 个人任务完成情况统计 |
| 任务列表 | 查看自己的任务数据，页面有状态变更按钮，可批量修改任务状态|
| 搁置任务列表 | 查看自己搁置的任务数据，页面有状态变更按钮，可批量修改任务状态|

### 4.2 管理员后台
| 功能 | 描述 |
|------|------|
| 用户管理 | 创建/编辑/删除用户，重置密码，禁用/启用用户 |
| 任务列表 | 查看所有用户的任务数据（只读） |
| 搁置任务列表 | 查看自己搁置的任务数据（只读） |
| 数据统计看板 | 用户数量、任务总数、完成率等统计数据 |

### 4.3 个人中心
| 功能 | 描述 |
|------|------|
| 修改密码 | 用户自行修改密码 |
| 个人信息 | 查看账号信息 |

## 5. 接口设计（初步规划）

### 5.1 认证模块
- `POST /api/auth/login` - 登录
- `POST /api/auth/logout` - 登出
- `POST /api/auth/refresh` - 刷新Token

### 5.2 用户模块（管理员）
- `GET /api/admin/users` - 用户列表
- `POST /api/admin/users` - 创建用户
- `PUT /api/admin/users/{id}` - 编辑用户
- `DELETE /api/admin/users/{id}` - 删除用户
- `POST /api/admin/users/{id}/reset-password` - 重置密码

### 5.3 任务模块
- `GET /api/tasks` - 任务列表（排除搁置）
- `GET /api/tasks/shelved` - 搁置任务列表
- `POST /api/tasks` - 创建任务
- `PUT /api/tasks/{id}` - 更新任务
- `PUT /api/tasks/batch-status` - 批量修改任务状态
- `DELETE /api/tasks/{id}` - 删除任务
- `GET /api/tasks/stats` - 任务统计

### 5.4 分组模块
- `GET /api/groups` - 分组列表
- `POST /api/groups` - 创建分组
- `PUT /api/groups/{id}` - 更新分组
- `DELETE /api/groups/{id}` - 删除分组

### 5.5 管理员统计
- `GET /api/admin/stats` - 系统统计概览
- `GET /api/admin/stats/tasks` - 任务统计详情
- `GET /api/admin/tasks` - 所有用户任务列表（只读）
- `GET /api/admin/tasks/shelved` - 所有用户搁置任务列表（只读）

## 6. 数据库设计（初步）

> 遵循数据库规范：字段全部小写字母，不使用下划线，布尔字段不以is开头

### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(255) | 加密密码 |
| nickname | VARCHAR(50) | 昵称 |
| role | VARCHAR(20) | 角色：admin/user |
| enabled | TINYINT(1) | 状态：1启用/0禁用 |
| deleted | TINYINT(1) | 删除标记：0正常/1已删 |
| createtime | DATETIME | 创建时间 |
| updatetime | DATETIME | 更新时间 |
| createuser | BIGINT | 创建人ID |
| updateuser | BIGINT | 更新人ID |

### 任务表 (task)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| userid | BIGINT | 用户ID |
| title | VARCHAR(200) | 任务标题 |
| description | TEXT | 任务描述 |
| groupid | BIGINT | 分组ID |
| priority | TINYINT | 优先级：1低/2中/3高/4紧急 |
| status | TINYINT | 状态：1待办/2进行中/3已完成/4搁置 |
| duedate | DATETIME | 截止日期 |
| completedtime | DATETIME | 完成时间 |
| deleted | TINYINT(1) | 删除标记：0正常/1已删 |
| createtime | DATETIME | 创建时间 |
| updatetime | DATETIME | 更新时间 |
| createuser | BIGINT | 创建人ID |
| updateuser | BIGINT | 更新人ID |

### 分组表 (group)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| userid | BIGINT | 用户ID（NULL表示系统默认分组） |
| name | VARCHAR(50) | 分组名称 |
| color | VARCHAR(10) | 分组颜色 |
| deleted | TINYINT(1) | 删除标记：0正常/1已删 |
| createtime | DATETIME | 创建时间 |
| updatetime | DATETIME | 更新时间 |
| createuser | BIGINT | 创建人ID |
| updateuser | BIGINT | 更新人ID |

## 7. 非功能需求

- **安全性**: 密码BCrypt加密，JWT Token认证，接口权限控制
- **性能**: Redis缓存用户Token和热点数据
- **可用性**: 响应式设计，支持PC和移动端

## 8. 扩展功能

暂不包含附件上传、任务评论、任务搜索、数据导出功能，后续按需迭代。