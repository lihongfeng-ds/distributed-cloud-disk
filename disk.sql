-- MySQL dump 10.13  Distrib 5.7.15, for Win64 (x86_64)
--
-- Host: localhost    Database: disk
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_file_share`
--

DROP TABLE IF EXISTS `t_file_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_file_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `share_code` varchar(10) NOT NULL COMMENT '提取码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file_share`
--

LOCK TABLES `t_file_share` WRITE;
/*!40000 ALTER TABLE `t_file_share` DISABLE KEYS */;
INSERT INTO `t_file_share` VALUES (1,'admin/HelloWorld.java','4sro','2026-10-20 09:26:35'),(2,'admin/HelloWorld.java','pzov','2026-10-20 09:28:14'),(3,'admin/lesson2.java','xs2f','2026-10-20 09:33:04'),(4,'root/java','6hy6','2026-10-20 13:34:32'),(5,'root/english.txt','vv9h','2026-10-20 13:34:41'),(6,'root/abc','pe7z','2026-10-20 13:40:54');
/*!40000 ALTER TABLE `t_file_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_operation_log`
--

DROP TABLE IF EXISTS `t_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `action` varchar(100) DEFAULT NULL COMMENT '操作类型',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_operation_log`
--

LOCK TABLES `t_operation_log` WRITE;
/*!40000 ALTER TABLE `t_operation_log` DISABLE KEYS */;
INSERT INTO `t_operation_log` VALUES (1,'admin','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:24:15'),(2,'admin','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:26:13'),(3,'admin','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:26:25'),(4,'root','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:26:44'),(5,'root','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:26:48'),(6,'root','上传文件','0:0:0:0:0:0:0:1','2026-10-20 15:27:14'),(7,'root','下载文件','0:0:0:0:0:0:0:1','2026-10-20 15:27:35'),(8,'root','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:31:22'),(9,'root','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:31:25'),(10,'root','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:31:27'),(11,'admin','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:31:43'),(12,'admin','用户登录','0:0:0:0:0:0:0:1','2026-10-20 15:31:47'),(13,'admin','用户登录','127.0.0.1','2026-10-20 15:32:22'),(14,'admin','用户登录','127.0.0.1','2026-10-20 15:42:03'),(15,'admin','用户登录','127.0.0.1','2026-10-20 15:42:23'),(16,'admin','用户登录','127.0.0.1','2026-10-20 15:42:56'),(17,'root','用户登录','127.0.0.1','2026-10-20 15:43:10'),(18,'root','用户登录','127.0.0.1','2026-10-20 15:43:13'),(19,'root','用户登录','127.0.0.1','2026-10-20 15:44:09'),(20,'root','删除文件','127.0.0.1','2026-10-20 15:44:27'),(21,'root','用户登录','127.0.0.1','2026-10-20 15:48:12'),(22,'root','用户登录','127.0.0.1','2026-10-20 15:48:12'),(23,'未知用户','注销退出','127.0.0.1','2026-10-20 15:48:12'),(24,'root','注销退出','127.0.0.1','2026-10-20 15:48:20'),(25,'root','用户登录','127.0.0.1','2026-10-20 15:48:32'),(26,'root','用户登录','127.0.0.1','2026-10-20 15:51:45'),(27,'root','用户登录','127.0.0.1','2026-10-20 15:51:49'),(28,'root','用户登录','127.0.0.1','2026-10-20 15:53:07'),(29,'root','用户登录','127.0.0.1','2026-10-20 15:53:24'),(30,'admin','用户登录','127.0.0.1','2026-10-20 15:53:50'),(31,'admin','用户登录','127.0.0.1','2026-10-20 15:53:54'),(32,'admin','用户登录','127.0.0.1','2026-10-20 16:06:22'),(33,'admin','用户登录','127.0.0.1','2026-10-20 16:06:38'),(34,'admin','用户登录','127.0.0.1','2026-10-20 16:06:53'),(35,'admin','用户登录','127.0.0.1','2026-10-20 16:06:56'),(36,'root','用户登录','127.0.0.1','2026-10-20 16:08:00'),(37,'root','用户登录','127.0.0.1','2026-10-20 16:08:14'),(38,'root','用户登录','127.0.0.1','2026-10-20 16:08:17'),(39,'admin','用户登录','127.0.0.1','2026-10-20 16:31:22'),(40,'admin','用户登录','127.0.0.1','2026-10-20 16:32:07'),(41,'admin','用户登录','127.0.0.1','2026-10-20 16:33:45'),(42,'admin','用户登录','127.0.0.1','2026-10-20 16:34:12'),(43,'root','用户登录','127.0.0.1','2026-10-20 16:36:08');
/*!40000 ALTER TABLE `t_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userinfo`
--

DROP TABLE IF EXISTS `userinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userinfo`
--

LOCK TABLES `userinfo` WRITE;
/*!40000 ALTER TABLE `userinfo` DISABLE KEYS */;
INSERT INTO `userinfo` VALUES (1,'root','root',NULL,'普通管理员'),(2,'赵强','123',NULL,'超级管理员'),(3,'车平平','9988',NULL,'中等管理员'),(4,'admin','123',NULL,NULL),(5,'代一凡','123','13566677888','师大学生'),(6,'7777','8888','122345543',NULL);
/*!40000 ALTER TABLE `userinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-21 12:50:02
