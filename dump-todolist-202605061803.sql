-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: todolist
-- ------------------------------------------------------
-- Server version	8.4.8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_groups`
--

DROP TABLE IF EXISTS `t_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_groups` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` bigint DEFAULT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '分组名称',
  `color` varchar(10) DEFAULT '#409EFF' COMMENT '分组颜色',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `createuser` bigint DEFAULT NULL COMMENT '创建人的id',
  `updateuser` bigint DEFAULT NULL COMMENT '更新人的id',
  PRIMARY KEY (`id`),
  KEY `idx_t_groups_userid` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_groups`
--

LOCK TABLES `t_groups` WRITE;
/*!40000 ALTER TABLE `t_groups` DISABLE KEYS */;
INSERT INTO `t_groups` VALUES (1,NULL,'默认','#409EFF',0,'2026-04-17 13:03:56','2026-04-17 13:03:56',NULL,NULL),(2,1,'美食','#67c23a',1,'2026-04-23 19:42:06','2026-04-23 11:49:13',1,1),(3,1,'运动','#f56c6c',0,'2026-04-23 19:45:19','2026-04-23 19:47:04',1,1),(4,1,'1','#409EFF',1,'2026-04-23 19:47:28','2026-04-23 11:47:33',1,1),(5,2,'张三的分组','#ff6040',0,'2026-04-23 20:14:40','2026-04-23 20:15:33',2,2);
/*!40000 ALTER TABLE `t_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_tasks`
--

DROP TABLE IF EXISTS `t_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_tasks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '任务标题',
  `description` text COMMENT '任务描述',
  `groupid` bigint DEFAULT NULL COMMENT '分组ID',
  `priority` tinyint DEFAULT '2' COMMENT '优先级：1低/2中/3高/4紧急',
  `status` tinyint DEFAULT '1' COMMENT '状态：1待办/2进行中/3已完成/4搁置',
  `duedate` datetime DEFAULT NULL COMMENT '截止日期',
  `completedtime` datetime DEFAULT NULL COMMENT '完成时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `createuser` bigint DEFAULT NULL COMMENT '创建人的id',
  `updateuser` bigint DEFAULT NULL COMMENT '更新人的id',
  PRIMARY KEY (`id`),
  KEY `idx_t_tasks_userid` (`userid`),
  KEY `idx_t_tasks_status` (`status`),
  KEY `idx_t_tasks_groupid` (`groupid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tasks`
--

LOCK TABLES `t_tasks` WRITE;
/*!40000 ALTER TABLE `t_tasks` DISABLE KEYS */;
INSERT INTO `t_tasks` VALUES (1,1,'喝酒','FDD发大幅度',2,1,1,'2026-04-23 16:00:00',NULL,0,'2026-04-23 18:57:07','2026-04-23 19:48:05',1,1),(2,1,'抽烟','辅导费地方',NULL,3,2,'2026-04-24 16:00:00',NULL,0,'2026-04-23 18:57:28','2026-04-23 19:02:11',1,1),(3,1,'烫头','',NULL,4,4,'2026-04-21 16:00:00',NULL,0,'2026-04-23 18:57:43','2026-04-23 19:01:39',1,1),(4,1,'打麻将','',NULL,2,1,'2026-04-29 16:00:00',NULL,0,'2026-04-23 19:18:55','2026-04-23 19:19:07',1,1),(5,1,'斗地主','',NULL,2,1,NULL,NULL,0,'2026-04-23 19:19:24','2026-04-23 19:19:24',1,1),(6,1,'爬山','',NULL,2,1,NULL,NULL,0,'2026-04-23 19:19:29','2026-04-23 19:19:29',1,1),(7,1,'写代码','',NULL,2,1,NULL,NULL,0,'2026-04-23 19:19:37','2026-04-23 19:19:37',1,1),(8,1,'看书','',NULL,2,1,NULL,NULL,1,'2026-04-23 19:19:44','2026-04-23 11:36:10',1,1),(9,1,'Complete project documentation','Write technical docs and user manual',NULL,3,4,NULL,NULL,0,'2026-04-23 19:21:35','2026-04-23 19:35:32',1,1),(10,1,'Code review and optimization','Review team code and optimize performance',NULL,4,2,NULL,NULL,0,'2026-04-23 19:21:35','2026-04-23 19:21:35',1,1),(11,1,'Handle user feedback','Process user submitted feedback and suggestions',NULL,2,3,NULL,NULL,0,'2026-04-23 19:21:35','2026-04-23 19:35:11',1,1),(12,1,'System testing and acceptance','Complete system testing and user acceptance',NULL,3,2,NULL,NULL,0,'2026-04-23 19:21:35','2026-04-23 19:21:35',1,1),(13,1,'Prepare for deployment','Prepare production environment deployment',3,4,3,NULL,NULL,0,'2026-04-23 19:21:35','2026-04-23 19:47:45',1,1),(14,1,'1','',2,2,1,NULL,NULL,0,'2026-04-23 19:37:16','2026-04-23 19:47:54',1,1),(15,2,'张三-任务1','',1,3,1,NULL,NULL,0,'2026-04-23 20:14:05','2026-04-23 20:14:05',2,2),(16,2,'张三-任务2','',5,2,2,'2026-04-29 16:00:00',NULL,0,'2026-04-23 20:14:21','2026-04-23 20:14:48',2,2),(17,2,'张三的搁置任务','',5,2,4,NULL,NULL,0,'2026-04-23 20:15:12','2026-04-23 20:15:18',2,2);
/*!40000 ALTER TABLE `t_tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_users`
--

DROP TABLE IF EXISTS `t_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '启用状态',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `createuser` bigint DEFAULT NULL COMMENT '创建人的id',
  `updateuser` bigint DEFAULT NULL COMMENT '更新人的id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_users`
--

LOCK TABLES `t_users` WRITE;
/*!40000 ALTER TABLE `t_users` DISABLE KEYS */;
INSERT INTO `t_users` VALUES (1,'admin','$2a$10$2WenNzy3S2nAG0N9B01YMuGMqEi4aZfV48B2PZhHZf1R19o4EQODq','管理员','admin',1,0,'2026-04-17 13:03:56','2026-04-23 10:33:18',NULL,NULL),(2,'zhangsan','$2a$10$d7r6irJNX6oLsNxnybepMORpCwKti2vXmYTY0tM3gpbJp3FbOq7eu','张三','user',1,0,'2026-04-23 19:58:56','2026-04-23 20:50:19',1,2);
/*!40000 ALTER TABLE `t_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'todolist'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-06 18:03:14
