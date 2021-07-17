-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: cti
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.14-MariaDB

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
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `html_text` mediumtext NOT NULL,
  `created_date` datetime NOT NULL,
  `question_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `approved` tinyint(1) NOT NULL,
  `comment` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answer_to_question` (`question_id`),
  KEY `fk_answer_detail_to_user` (`user_id`),
  CONSTRAINT `fk_answer_detail_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_answer_to_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer_attachment`
--

DROP TABLE IF EXISTS `answer_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(256) NOT NULL,
  `answer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answer_attachment_to_answer` (`answer_id`),
  CONSTRAINT `fk_answer_attachment_to_answer` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_attachment`
--

LOCK TABLES `answer_attachment` WRITE;
/*!40000 ALTER TABLE `answer_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer_vote`
--

DROP TABLE IF EXISTS `answer_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_vote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote` tinyint(1) NOT NULL,
  `answer_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_answer_vote_to_answer` (`answer_id`),
  KEY `fk_answer_vote_to_user` (`user_id`),
  CONSTRAINT `fk_answer_vote_to_answer` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`),
  CONSTRAINT `fk_answer_vote_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_vote`
--

LOCK TABLES `answer_vote` WRITE;
/*!40000 ALTER TABLE `answer_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Math'),(2,'English'),(3,'Science'),(4,'Physics'),(5,'Chemistry'),(6,'Biology'),(7,'History'),(8,'Geography'),(9,'Music'),(10,'Art'),(11,'Religion'),(12,'Computer Science');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_log`
--

DROP TABLE IF EXISTS `event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action` varchar(32) NOT NULL,
  `info` varchar(256) NOT NULL,
  `created_date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_log_to_user` (`user_id`),
  CONSTRAINT `fk_event_log_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_log`
--

LOCK TABLES `event_log` WRITE;
/*!40000 ALTER TABLE `event_log` DISABLE KEYS */;
INSERT INTO `event_log` VALUES (1,'update','Changed account status from \"0\" to \"1\" of user with id 1','2021-06-11 01:23:00',1),(2,'select','Logged in user with id 1','2021-06-11 01:24:39',1),(3,'update','Changed roles of user with id 1','2021-06-11 01:24:58',1),(4,'select','Logged out user with id 1','2021-06-11 01:25:38',1),(5,'select','Logged in user with id 1','2021-06-11 01:26:21',1),(6,'update','Changed account status from \"0\" to \"1\" of user with id 2','2021-06-11 01:26:38',2),(7,'update','Changed roles of user with id 2','2021-06-11 01:27:18',1),(8,'select','Logged out user with id 1','2021-06-11 01:27:41',1),(9,'select','Logged in user with id 1','2021-06-11 01:28:08',1),(10,'update','Changed account status from \"0\" to \"1\" of user with id 3','2021-06-11 01:28:25',3),(11,'update','Changed roles of user with id 3','2021-06-11 01:28:38',1),(12,'select','Logged out user with id 1','2021-06-11 01:28:54',1),(13,'update','Changed account status from \"0\" to \"1\" of user with id 4','2021-06-11 01:29:40',4),(14,'select','Logged in user with id 1','2021-06-11 01:29:51',1),(15,'update','Changed roles of user with id 4','2021-06-11 01:30:07',1),(16,'select','Logged out user with id 1','2021-06-11 01:30:11',1),(17,'update','Changed account status from \"0\" to \"1\" of user with id 5','2021-06-11 01:31:02',5),(18,'select','Logged in user with id 1','2021-06-11 01:31:06',1),(19,'update','Changed roles of user with id 5','2021-06-11 01:31:16',1),(20,'select','Logged out user with id 1','2021-06-11 01:31:23',1),(21,'select','Logged in user with id 3','2021-06-11 01:32:03',3),(22,'insert','Created category with id 1','2021-06-11 01:32:41',3),(23,'insert','Created category with id 2','2021-06-11 01:32:45',3),(24,'insert','Created category with id 3','2021-06-11 01:32:49',3),(25,'insert','Created category with id 4','2021-06-11 01:32:52',3),(26,'insert','Created category with id 5','2021-06-11 01:32:56',3),(27,'insert','Created category with id 6','2021-06-11 01:32:59',3),(28,'insert','Created category with id 7','2021-06-11 01:33:03',3),(29,'insert','Created category with id 8','2021-06-11 01:33:07',3),(30,'insert','Created category with id 9','2021-06-11 01:33:10',3),(31,'insert','Created category with id 10','2021-06-11 01:33:13',3),(32,'insert','Created category with id 11','2021-06-11 01:33:16',3),(33,'insert','Created category with id 12','2021-06-11 01:33:20',3);
/*!40000 ALTER TABLE `event_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `content` text DEFAULT NULL,
  `viewed` tinyint(1) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `url` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_to_user` (`user_id`),
  CONSTRAINT `fk_notification_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:22:51',1,'/home/profile/notifications/view-notification/1'),(2,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:26:21',2,'/home/profile/notifications/view-notification/2'),(3,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:28:12',3,'/home/profile/notifications/view-notification/3'),(4,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:29:35',4,'/home/profile/notifications/view-notification/4'),(5,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:30:48',5,'/home/profile/notifications/view-notification/5');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(256) NOT NULL,
  `html_text` mediumtext DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `category_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `approved` tinyint(1) NOT NULL,
  `comment` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_to_category` (`category_id`),
  KEY `fk_question_to_user` (`user_id`),
  CONSTRAINT `fk_question_to_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_question_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_attachment`
--

DROP TABLE IF EXISTS `question_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(256) NOT NULL,
  `question_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_attachment_to_question` (`question_id`),
  CONSTRAINT `fk_question_attachment_to_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_attachment`
--

LOCK TABLES `question_attachment` WRITE;
/*!40000 ALTER TABLE `question_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_vote`
--

DROP TABLE IF EXISTS `question_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_vote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote` tinyint(1) NOT NULL,
  `question_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_vote_to_question` (`question_id`),
  KEY `fk_question_vote_to_user` (`user_id`),
  CONSTRAINT `fk_question_vote_to_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  CONSTRAINT `fk_question_vote_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_vote`
--

LOCK TABLES `question_vote` WRITE;
/*!40000 ALTER TABLE `question_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `question_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'teacher'),(2,'reviewer'),(3,'moderator'),(4,'administrator');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(320) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `created_date` datetime NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'superuser','{noop}abcdef123','superuser@email.com',NULL,NULL,'2021-06-11 01:22:40',1),(2,'admin','{noop}abcdef123','admin@email.com',NULL,NULL,'2021-06-11 01:26:11',1),(3,'moderator','{noop}abcdef123','moderator@email.com',NULL,NULL,'2021-06-11 01:28:01',1),(4,'reviewer','{noop}abcdef123','reviewer@email.com',NULL,NULL,'2021-06-11 01:29:24',1),(5,'teacher','{noop}abcdef123','teacher@email.com',NULL,NULL,'2021-06-11 01:30:38',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `fk_user_role_to_user` (`user_id`),
  KEY `fk_user_role_to_role` (`role_id`),
  CONSTRAINT `fk_user_role_to_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_role_to_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(1,2),(1,3),(1,4),(2,4),(3,3),(4,2),(5,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-11  1:34:33
