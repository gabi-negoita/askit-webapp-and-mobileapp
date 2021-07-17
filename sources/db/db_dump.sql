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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,'<p>Faci mai intai operatiile din paranteza, apoi inmultesti rezultatul cu 2</p>\r\n<p>Pasi rezolvare:</p>\r\n<ul>\r\n<li>(<span style=\"text-decoration: underline;\">234+15</span>-20+5)X2</li>\r\n<li>= (<span style=\"text-decoration: underline;\"><strong>249</strong>-20</span>+5)X2</li>\r\n<li>= (<span style=\"text-decoration: underline;\"><strong>229</strong> + 5</span>)X2</li>\r\n<li>= <span style=\"text-decoration: underline;\"><strong>234</strong> X 2</span></li>\r\n<li>= <strong>468</strong></li>\r\n</ul>','2021-06-11 12:12:06',1,1,1,NULL),(2,'<p>1914 - 1918 primul razboi mondial</p>','2021-06-12 22:27:12',3,7,1,''),(3,'<p>Perioada dintre cele doua razboaie mondiale: 1918 - 1934?</p>','2021-06-12 22:31:38',4,7,1,''),(4,'<p>Perioada dintre cele doua razboaie mondiale: 1918 - 1934?</p>','2021-06-12 22:33:58',4,7,-1,'Deja ai răspuns la această întrebare. - Gabi'),(5,'<p>1913 - 1918 a avut loc primul razboi mondial</p>','2021-06-12 22:34:28',3,8,1,NULL),(6,'<p>1934? - 1944 al doilea razboi mondial</p>','2021-06-12 22:35:28',3,7,-1,'Informații false :O'),(7,'<p>Perioada interbelica reprezinta perioada dintre cele doua razboaie monsiale.</p>','2021-06-12 22:37:24',4,8,1,NULL),(8,'<p>2x=3+1</p>\r\n<p>X=4:2</p>\r\n<p>X=2</p>','2021-06-12 22:45:37',2,8,1,NULL),(9,'<p>Stefan cel mare a fost voievod al Modovei intre&nbsp;<span style=\"background-color: #ffffff; color: #202122; font-family: sans-serif; font-size: 14px;\">1457 si 1504</span></p>','2021-06-12 22:54:44',6,1,1,NULL),(10,'<p>Lacul Capra ar fi un raspuns</p>','2021-06-12 22:56:01',8,1,1,NULL),(11,'<p>Un algoritm reprezintă un set de pasi bine definiti pe care cineva poate sa-i urmeze pentru a rezolva o problemă anume.</p>','2021-06-12 22:59:31',9,9,1,''),(14,'<p>un ip este o adresa prin care se poate identifica un dispozitiv in retea</p>','2021-06-23 18:42:58',5,9,1,NULL),(15,'<p>un ip este o adresa prin care se poate identifica un dispozitiv in retea</p>','2021-06-23 18:43:36',5,9,-1,'Raspunsul este duplicat. Ați răspuns deja la această întrebare.'),(16,'<p>o adresa care poater fi accesata prin retea</p>','2021-06-24 14:02:16',5,9,-1,NULL),(17,'<p><span style=\"color: #2dc26b;\">SUBIECT </span>+ <span style=\"color: #3598db;\">TO BE</span> (conjugat la prezent) + <span style=\"color: #e67e23;\">VERB-ing</span> (verb de conjugat cu terminatia \"ing\")</p>\r\n<p>E.g.: <span style=\"color: #2dc26b;\">I</span> <span style=\"color: #3598db;\">am </span><span style=\"color: #e67e23;\">reading</span>.</p>\r\n<p>I = subuiect</p>\r\n<p>am = verb to be conjugat la prezent pentru persoana I singular</p>\r\n<p>reading = verbul read + terminatia ing</p>','2021-06-25 10:58:26',18,9,1,NULL),(18,'<p>Subiect + verb conjugat la prezent</p>\r\n<p>E.g.: I work.</p>','2021-06-25 11:05:35',19,9,1,NULL),(19,'<p>adresa unui dispozitiv si are forma XYZ.XYZ.XYZ.XYZ, unde XYZ au valor intre 1 si 255</p>','2021-06-30 15:00:29',5,1,1,NULL),(20,'<p><span style=\"color: #4d5156; font-family: arial, sans-serif; font-size: 14px; background-color: #ffffff;\">A mitochondrion is a double membrane-bound organelle found in most eukaryotic organisms. Mitochondria generate most of the cell\'s supply of adenosine triphosphate, used as a source of chemical energy. Mitochondria were first discovered by Kolliker in the voluntary muscles of insects.</span></p>','2021-06-30 15:02:12',12,9,0,NULL),(21,'<p>Cred ca se poate realiza printr-un hub</p>','2021-06-30 16:20:52',14,9,0,NULL),(22,'<p>perioada dintre cele doua razboaie mondiale</p>','2021-06-30 16:24:09',4,9,0,NULL),(23,'1914-1918 și 1939-1945','2021-06-30 22:19:50',3,2,0,NULL),(24,'<p>primul razboi mondial a fost intre anii: 1914-1918</p>','2021-07-08 18:25:56',3,21,1,NULL),(25,'<p>Capitala Angliei este <strong>Londra</strong></p>','2021-07-08 18:30:52',23,5,1,NULL),(26,'rezolva operatiile din paranteza apoi inmulteste cu 2 rezultatul','2021-07-10 00:03:48',1,21,0,NULL),(27,'dupa ce rezolvi operatia din paranteza, inmulteste cu 2 ','2021-07-10 00:05:54',1,5,1,NULL),(28,'rezolva paranteza','2021-07-10 00:13:27',1,21,0,NULL),(29,'calculatoarele se pot conecta printr-un hub','2021-07-10 00:15:21',14,5,1,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_vote`
--

LOCK TABLES `answer_vote` WRITE;
/*!40000 ALTER TABLE `answer_vote` DISABLE KEYS */;
INSERT INTO `answer_vote` VALUES (1,1,1,6),(4,1,7,1),(5,-1,3,1),(6,1,8,1),(7,1,10,8),(8,1,11,8),(9,1,9,8),(10,-1,5,1),(11,1,2,1),(22,-1,5,8),(23,1,2,21),(24,-1,5,21),(25,-1,1,1),(26,1,27,5);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
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
) ENGINE=InnoDB AUTO_INCREMENT=467 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_log`
--

LOCK TABLES `event_log` WRITE;
/*!40000 ALTER TABLE `event_log` DISABLE KEYS */;
INSERT INTO `event_log` VALUES (1,'update','Changed account status from \"0\" to \"1\" of user with id 1','2021-06-11 01:23:00',1),(2,'select','Logged in user with id 1','2021-06-11 01:24:39',1),(3,'update','Changed roles of user with id 1','2021-06-11 01:24:58',1),(4,'select','Logged out user with id 1','2021-06-11 01:25:38',1),(5,'select','Logged in user with id 1','2021-06-11 01:26:21',1),(6,'update','Changed account status from \"0\" to \"1\" of user with id 2','2021-06-11 01:26:38',2),(7,'update','Changed roles of user with id 2','2021-06-11 01:27:18',1),(8,'select','Logged out user with id 1','2021-06-11 01:27:41',1),(9,'select','Logged in user with id 1','2021-06-11 01:28:08',1),(10,'update','Changed account status from \"0\" to \"1\" of user with id 3','2021-06-11 01:28:25',3),(11,'update','Changed roles of user with id 3','2021-06-11 01:28:38',1),(12,'select','Logged out user with id 1','2021-06-11 01:28:54',1),(13,'update','Changed account status from \"0\" to \"1\" of user with id 4','2021-06-11 01:29:40',4),(14,'select','Logged in user with id 1','2021-06-11 01:29:51',1),(15,'update','Changed roles of user with id 4','2021-06-11 01:30:07',1),(16,'select','Logged out user with id 1','2021-06-11 01:30:11',1),(17,'update','Changed account status from \"0\" to \"1\" of user with id 5','2021-06-11 01:31:02',5),(18,'select','Logged in user with id 1','2021-06-11 01:31:06',1),(19,'update','Changed roles of user with id 5','2021-06-11 01:31:16',1),(20,'select','Logged out user with id 1','2021-06-11 01:31:23',1),(21,'select','Logged in user with id 3','2021-06-11 01:32:03',3),(22,'insert','Created category with id 1','2021-06-11 01:32:41',3),(23,'insert','Created category with id 2','2021-06-11 01:32:45',3),(24,'insert','Created category with id 3','2021-06-11 01:32:49',3),(25,'insert','Created category with id 4','2021-06-11 01:32:52',3),(26,'insert','Created category with id 5','2021-06-11 01:32:56',3),(27,'insert','Created category with id 6','2021-06-11 01:32:59',3),(28,'insert','Created category with id 7','2021-06-11 01:33:03',3),(29,'insert','Created category with id 8','2021-06-11 01:33:07',3),(30,'insert','Created category with id 9','2021-06-11 01:33:10',3),(31,'insert','Created category with id 10','2021-06-11 01:33:13',3),(32,'insert','Created category with id 11','2021-06-11 01:33:16',3),(33,'insert','Created category with id 12','2021-06-11 01:33:20',3),(34,'select','Logged out user with id 3','2021-06-11 01:36:02',3),(35,'update','Changed account status from \"0\" to \"1\" of user with id 6','2021-06-11 11:52:44',6),(36,'select','Logged in user with id 6','2021-06-11 11:53:11',6),(37,'select','Logged in user with id 1','2021-06-11 11:54:45',1),(38,'update','Modified user profile info with id 6','2021-06-11 11:57:50',6),(39,'update','Modified user profile info with id 6','2021-06-11 11:59:03',6),(40,'update','Changed roles of user with id 6','2021-06-11 11:59:31',1),(41,'insert','Posted question with id 1','2021-06-11 12:05:27',6),(42,'insert','Posted question with id 2','2021-06-11 12:05:51',1),(43,'update','Changed approved from \"0\" to \"1\" of question with id 1','2021-06-11 12:06:19',1),(44,'select','Logged in user with id 1','2021-06-11 12:07:35',1),(45,'update','Posted answer with id 1','2021-06-11 12:12:06',1),(46,'select','Logged in user with id 6','2021-06-11 12:13:10',6),(47,'update','Modified user profile info with id 6','2021-06-11 12:23:09',6),(48,'select','Logged in user with id 1','2021-06-11 12:25:19',1),(49,'insert','Posted question with id 3','2021-06-11 12:27:11',1),(50,'insert','Posted question with id 4','2021-06-11 12:28:00',1),(51,'select','Logged in user with id 1','2021-06-11 14:37:43',1),(52,'select','Logged out user with id 1','2021-06-11 14:45:07',1),(53,'select','Logged in user with id 1','2021-06-11 17:07:58',1),(54,'insert','Posted question with id 5','2021-06-11 17:08:37',1),(55,'select','Logged in user with id 3','2021-06-12 14:12:31',3),(56,'select','Logged out user with id 3','2021-06-12 14:27:11',3),(57,'select','Logged in user with id 2','2021-06-12 14:27:14',2),(58,'select','Logged in user with id 1','2021-06-12 21:57:48',1),(59,'update','Changed account status from \"0\" to \"1\" of user with id 7','2021-06-12 22:24:48',7),(60,'update','Changed account status from \"0\" to \"1\" of user with id 8','2021-06-12 22:24:49',8),(61,'select','Logged in user with id 7','2021-06-12 22:25:04',7),(62,'update','Posted answer with id 2','2021-06-12 22:27:12',7),(63,'update','Changed roles of user with id 8','2021-06-12 22:29:37',1),(64,'update','Changed roles of user with id 7','2021-06-12 22:29:45',1),(65,'update','Changed approved from \"0\" to \"1\" of answer with id 2','2021-06-12 22:30:01',1),(66,'update','Posted answer with id 3','2021-06-12 22:31:38',7),(67,'select','Logged in user with id 8','2021-06-12 22:33:09',8),(68,'update','Posted answer with id 4','2021-06-12 22:33:58',7),(69,'update','Posted answer with id 5','2021-06-12 22:34:28',8),(70,'update','Posted answer with id 6','2021-06-12 22:35:28',7),(71,'update','Changed approved from \"0\" to \"1\" of answer with id 3','2021-06-12 22:37:20',1),(72,'update','Posted answer with id 7','2021-06-12 22:37:24',8),(73,'update','Changed approved from \"0\" to \"-1\" of answer with id 4','2021-06-12 22:37:51',1),(74,'update','Changed approved from \"0\" to \"-1\" of answer with id 6','2021-06-12 22:38:22',1),(75,'select','Logged out user with id 8','2021-06-12 22:41:17',8),(76,'select','Logged in user with id 8','2021-06-12 22:41:31',8),(77,'update','Posted answer with id 8','2021-06-12 22:45:37',8),(78,'select','Logged in user with id 7','2021-06-12 22:46:52',7),(79,'insert','Posted question with id 6','2021-06-12 22:48:01',7),(80,'insert','Posted question with id 7','2021-06-12 22:49:36',8),(81,'insert','Posted question with id 8','2021-06-12 22:50:36',8),(82,'update','Posted answer with id 9','2021-06-12 22:54:44',1),(83,'update','Posted answer with id 10','2021-06-12 22:56:01',1),(84,'insert','Posted question with id 9','2021-06-12 22:56:25',8),(85,'select','Logged out user with id 1','2021-06-12 22:56:51',1),(86,'update','Changed account status from \"0\" to \"1\" of user with id 9','2021-06-12 22:57:20',9),(87,'select','Logged in user with id 9','2021-06-12 22:57:38',9),(88,'update','Posted answer with id 11','2021-06-12 22:59:31',9),(89,'select','Logged out user with id 9','2021-06-12 22:59:41',9),(90,'select','Logged in user with id 1','2021-06-12 22:59:42',1),(91,'select','Logged out user with id 1','2021-06-12 22:59:49',1),(92,'select','Logged in user with id 4','2021-06-12 22:59:51',4),(93,'update','Changed approved from \"0\" to \"1\" of answer with id 11','2021-06-12 22:59:57',4),(94,'select','Logged out user with id 4','2021-06-12 23:00:38',4),(95,'select','Logged in user with id 9','2021-06-12 23:00:49',9),(96,'select','Logged out user with id 9','2021-06-12 23:13:08',9),(97,'select','Logged in user with id 1','2021-06-12 23:13:16',1),(98,'select','Logged in user with id 1','2021-06-12 23:15:05',1),(99,'select','Logged in user with id 4','2021-06-13 12:30:02',4),(100,'select','Logged out user with id 4','2021-06-13 12:30:08',4),(101,'select','Logged in user with id 2','2021-06-13 12:30:19',2),(102,'select','Logged in user with id 2','2021-06-13 12:30:33',2),(103,'select','Logged in user with id 2','2021-06-13 12:31:52',2),(104,'select','Logged in user with id 2','2021-06-13 12:32:29',2),(105,'select','Logged in user with id 1','2021-06-13 12:33:59',1),(106,'select','Logged in user with id 1','2021-06-13 12:34:42',1),(107,'select','Logged in user with id 1','2021-06-13 12:39:05',1),(108,'select','Logged in user with id 1','2021-06-13 12:40:20',1),(109,'select','Logged in user with id 1','2021-06-13 12:41:03',1),(110,'select','Logged in user with id 1','2021-06-13 12:54:18',1),(111,'select','Logged in user with id 1','2021-06-13 12:55:48',1),(112,'select','Logged in user with id 4','2021-06-14 19:03:10',4),(113,'select','Logged in user with id 9','2021-06-14 19:03:32',9),(114,'update','Posted answer with id 12','2021-06-14 19:03:47',9),(115,'update','Changed approved from \"0\" to \"-1\" of answer with id 12','2021-06-14 21:16:35',4),(116,'update','Modified answer with id 12','2021-06-14 21:17:07',9),(117,'update','Modified answer with id 12','2021-06-14 21:17:36',9),(118,'select','Logged in user with id 9','2021-06-14 21:20:45',9),(119,'update','Modified answer with id 12','2021-06-14 21:20:53',9),(120,'select','Logged in user with id 4','2021-06-14 21:21:03',4),(121,'select','Logged in user with id 9','2021-06-14 21:21:53',9),(122,'update','Modified answer with id 12','2021-06-14 21:22:09',9),(123,'select','Logged in user with id 4','2021-06-14 21:22:17',4),(124,'update','Changed approved from \"0\" to \"1\" of answer with id 12','2021-06-14 21:22:38',4),(125,'select','Logged in user with id 9','2021-06-14 21:26:54',9),(126,'update','Modified answer with id 12','2021-06-14 21:27:07',9),(127,'select','Logged in user with id 4','2021-06-14 21:27:26',4),(128,'update','Changed approved from \"0\" to \"1\" of answer with id 12','2021-06-14 21:27:40',4),(129,'select','Logged in user with id 4','2021-06-15 16:40:10',4),(130,'update','Modified user profile info with id 4','2021-06-15 18:15:13',4),(131,'update','Modified user profile info with id 4','2021-06-15 18:15:20',4),(132,'update','Modified user profile info with id 4','2021-06-15 18:15:24',4),(133,'update','Changed email from \"reviewer@email.com\" to \"negoita.gabi@yahoo.com\"','2021-06-15 18:17:33',4),(134,'select','Logged in user with id 4','2021-06-15 18:30:06',4),(135,'select','Logged out user with id 4','2021-06-15 19:57:54',4),(136,'select','Logged in user with id 1','2021-06-15 19:57:58',1),(137,'insert','Sent notification with id 31','2021-06-15 20:00:22',1),(138,'select','Logged out user with id 1','2021-06-15 20:00:50',1),(139,'select','Logged in user with id 4','2021-06-15 20:00:52',4),(140,'select','Logged in user with id 1','2021-06-15 20:02:31',1),(141,'select','Logged in user with id 4','2021-06-15 20:05:50',4),(142,'select','Logged in user with id 4','2021-06-15 20:07:36',4),(143,'select','Logged out user with id 4','2021-06-15 22:26:43',4),(144,'select','Logged in user with id 4','2021-06-15 22:26:47',4),(145,'select','Logged in user with id 4','2021-06-15 22:58:03',4),(146,'select','Logged out user with id 4','2021-06-15 23:00:36',4),(147,'select','Logged in user with id 1','2021-06-15 23:00:38',1),(148,'select','Logged in user with id 1','2021-06-15 23:05:12',1),(149,'select','Logged in user with id 1','2021-06-15 23:05:21',1),(150,'select','Logged in user with id 1','2021-06-15 23:07:58',1),(151,'select','Logged in user with id 1','2021-06-15 23:08:25',1),(152,'select','Logged in user with id 1','2021-06-15 23:09:19',1),(153,'select','Logged in user with id 1','2021-06-15 23:12:20',1),(154,'select','Logged in user with id 4','2021-06-15 23:14:20',4),(155,'select','Logged in user with id 4','2021-06-15 23:18:46',4),(156,'select','Logged in user with id 4','2021-06-15 23:26:14',4),(157,'select','Logged in user with id 4','2021-06-15 23:27:43',4),(158,'select','Logged in user with id 4','2021-06-15 23:31:00',4),(159,'select','Logged in user with id 4','2021-06-15 23:32:06',4),(160,'select','Logged in user with id 4','2021-06-15 23:34:06',4),(161,'select','Logged in user with id 4','2021-06-15 23:46:45',4),(162,'select','Logged in user with id 4','2021-06-15 23:49:39',4),(163,'select','Logged in user with id 4','2021-06-15 23:50:46',4),(164,'select','Logged in user with id 4','2021-06-15 23:51:58',4),(165,'select','Logged in user with id 4','2021-06-15 23:52:52',4),(166,'select','Logged in user with id 4','2021-06-15 23:53:33',4),(167,'select','Logged in user with id 4','2021-06-15 23:56:04',4),(168,'select','Logged in user with id 4','2021-06-16 00:00:56',4),(169,'select','Logged in user with id 1','2021-06-16 13:39:02',1),(170,'select','Logged in user with id 1','2021-06-16 13:49:41',1),(171,'select','Logged in user with id 1','2021-06-16 13:51:21',1),(172,'select','Logged in user with id 1','2021-06-16 13:52:43',1),(173,'select','Logged in user with id 1','2021-06-16 13:54:54',1),(174,'select','Logged in user with id 1','2021-06-16 13:57:00',1),(175,'select','Logged in user with id 1','2021-06-16 13:57:56',1),(176,'insert','Posted question with id 10','2021-06-16 14:21:42',1),(177,'select','Logged in user with id 1','2021-06-16 16:04:04',1),(178,'select','Logged out user with id 1','2021-06-16 16:26:17',1),(179,'select','Logged in user with id 1','2021-06-16 16:56:30',1),(180,'select','Logged in user with id 4','2021-06-16 17:54:54',4),(181,'select','Logged in user with id 9','2021-06-16 18:09:28',9),(182,'select','Logged out user with id 4','2021-06-16 18:11:57',4),(183,'select','Logged in user with id 9','2021-06-16 18:12:03',9),(184,'insert','Posted question with id 11','2021-06-16 18:12:14',9),(185,'update','Posted answer with id 13','2021-06-16 18:13:07',9),(186,'select','Logged out user with id 9','2021-06-16 18:13:20',9),(187,'select','Logged in user with id 4','2021-06-16 18:13:25',4),(188,'select','Logged in user with id 4','2021-06-16 18:33:19',4),(189,'select','Logged in user with id 4','2021-06-16 18:34:01',4),(190,'select','Logged in user with id 1','2021-06-17 13:23:57',1),(191,'select','Logged in user with id 1','2021-06-17 15:35:08',1),(192,'select','Logged in user with id 1','2021-06-17 18:29:49',1),(193,'select','Logged in user with id 1','2021-06-17 18:36:19',1),(194,'select','Logged in user with id 1','2021-06-17 18:37:10',1),(195,'select','Logged in user with id 1','2021-06-17 18:38:05',1),(196,'select','Logged in user with id 1','2021-06-17 18:39:32',1),(197,'select','Logged in user with id 1','2021-06-17 18:40:42',1),(198,'select','Logged in user with id 1','2021-06-17 18:41:17',1),(199,'select','Logged in user with id 1','2021-06-17 18:42:15',1),(200,'select','Logged in user with id 1','2021-06-17 18:42:45',1),(201,'select','Logged in user with id 1','2021-06-17 18:43:20',1),(202,'select','Logged in user with id 1','2021-06-17 18:48:57',1),(203,'select','Logged in user with id 1','2021-06-17 18:51:15',1),(204,'select','Logged in user with id 1','2021-06-17 18:56:56',1),(205,'select','Logged in user with id 1','2021-06-17 18:57:54',1),(206,'select','Logged in user with id 1','2021-06-17 19:01:34',1),(207,'select','Logged in user with id 1','2021-06-17 19:13:14',1),(208,'select','Logged in user with id 1','2021-06-17 19:14:30',1),(209,'select','Logged in user with id 1','2021-06-17 19:23:42',1),(210,'select','Logged in user with id 1','2021-06-17 19:26:02',1),(211,'select','Logged in user with id 4','2021-06-17 19:30:35',4),(212,'select','Logged out user with id 4','2021-06-17 19:30:48',4),(213,'select','Logged in user with id 1','2021-06-17 19:30:54',1),(214,'select','Logged in user with id 1','2021-06-18 16:49:10',1),(215,'insert','Posted question with id 12','2021-06-18 16:59:34',1),(216,'insert','Sent notification with id 33','2021-06-18 17:04:39',1),(217,'select','Logged in user with id 1','2021-06-18 17:19:10',1),(218,'select','Logged in user with id 1','2021-06-18 18:26:00',1),(219,'select','Logged in user with id 1','2021-06-19 17:59:57',1),(220,'update','Changed email from \"superuser@email.com\" to \"negoita.gabi@yahoo.com\"','2021-06-19 18:01:27',1),(221,'select','Logged out user with id 1','2021-06-19 18:01:45',1),(222,'select','Logged in user with id 1','2021-06-19 18:01:51',1),(223,'update','Changed user password with id 1','2021-06-19 18:02:28',1),(224,'select','Logged out user with id 1','2021-06-19 18:05:33',1),(225,'update','Changed account status from \"0\" to \"1\" of user with id 9','2021-06-19 18:25:10',9),(226,'select','Logged in user with id 9','2021-06-20 13:01:33',9),(227,'insert','Posted question with id 13','2021-06-20 13:02:55',9),(228,'select','Logged out user with id 9','2021-06-20 13:14:28',9),(229,'select','Logged in user with id 1','2021-06-20 13:14:32',1),(230,'select','Logged in user with id 1','2021-06-21 19:21:30',1),(231,'select','Logged in user with id 1','2021-06-22 17:42:23',1),(232,'select','Logged out user with id 1','2021-06-22 17:42:28',1),(233,'select','Logged in user with id 9','2021-06-23 11:00:21',9),(234,'select','Logged out user with id 9','2021-06-23 11:05:34',9),(235,'select','Logged in user with id 1','2021-06-23 11:14:11',1),(236,'select','Logged out user with id 1','2021-06-23 11:14:33',1),(237,'select','Logged in user with id 9','2021-06-23 11:14:42',9),(238,'select','Logged out user with id 9','2021-06-23 11:38:51',9),(239,'select','Logged in user with id 4','2021-06-23 11:38:56',4),(240,'select','Logged out user with id 4','2021-06-23 11:40:16',4),(241,'select','Logged in user with id 9','2021-06-23 11:57:14',9),(242,'select','Logged out user with id 9','2021-06-23 12:13:06',9),(243,'select','Logged in user with id 1','2021-06-23 12:13:09',1),(244,'insert','Posted question with id 14','2021-06-23 12:34:19',1),(245,'select','Logged out user with id 1','2021-06-23 12:40:27',1),(246,'update','Changed password of user with id 9','2021-06-23 15:05:38',9),(247,'update','Changed password of user with id 9','2021-06-23 15:05:53',9),(248,'update','Changed password of user with id 9','2021-06-23 15:06:04',9),(249,'update','Changed password of user with id 9','2021-06-23 15:06:24',9),(250,'update','Changed password of user with id 9','2021-06-23 15:06:56',9),(251,'update','Changed account status from \"0\" to \"1\" of user with id 10','2021-06-23 15:28:04',10),(252,'select','Logged in user with id 10','2021-06-23 15:41:13',10),(253,'select','Logged out user with id 10','2021-06-23 15:41:36',10),(254,'select','Logged in user with id 9','2021-06-23 15:42:46',9),(255,'select','Logged in user with id 1','2021-06-23 18:21:31',1),(256,'select','Logged out user with id 1','2021-06-23 18:42:19',1),(257,'select','Logged in user with id 9','2021-06-23 18:42:26',9),(258,'update','Posted answer with id 14','2021-06-23 18:42:58',9),(259,'update','Posted answer with id 15','2021-06-23 18:43:36',9),(260,'insert','Posted question with id 15','2021-06-23 18:54:16',9),(261,'select','Logged out user with id 9','2021-06-23 18:54:34',9),(262,'select','Logged in user with id 4','2021-06-23 18:54:38',4),(263,'select','Logged out user with id 4','2021-06-23 19:27:08',4),(264,'select','Logged in user with id 1','2021-06-23 19:27:11',1),(265,'select','Logged in user with id 1','2021-06-23 21:22:40',1),(266,'select','Logged out user with id 1','2021-06-23 21:22:52',1),(267,'select','Logged in user with id 9','2021-06-23 21:23:05',9),(268,'select','Logged out user with id 9','2021-06-23 21:23:22',9),(269,'select','Logged in user with id 4','2021-06-23 21:23:30',4),(270,'select','Logged out user with id 4','2021-06-23 21:23:35',4),(271,'select','Logged in user with id 1','2021-06-23 21:23:39',1),(272,'update','Changed approved from \"0\" to \"-1\" of answer with id 15','2021-06-23 21:24:55',1),(273,'select','Logged out user with id 1','2021-06-23 21:25:01',1),(274,'select','Logged in user with id 9','2021-06-23 21:25:12',9),(275,'select','Logged out user with id 9','2021-06-23 21:38:03',9),(276,'select','Logged in user with id 1','2021-06-23 21:38:06',1),(277,'select','Logged out user with id 1','2021-06-23 21:41:42',1),(278,'select','Logged in user with id 9','2021-06-23 21:41:48',9),(279,'select','Logged out user with id 9','2021-06-23 21:57:34',9),(280,'select','Logged in user with id 9','2021-06-24 13:05:49',9),(281,'update','Modified user profile info with id 9','2021-06-24 13:06:50',9),(282,'select','Logged out user with id 9','2021-06-24 13:36:15',9),(283,'select','Logged in user with id 1','2021-06-24 13:36:17',1),(284,'update','Changed approved from \"0\" to \"-1\" of question with id 15','2021-06-24 13:44:34',1),(285,'select','Logged in user with id 9','2021-06-24 14:00:21',9),(286,'insert','Posted question with id 16','2021-06-24 14:01:15',9),(287,'insert','Posted question with id 17','2021-06-24 14:01:43',9),(288,'update','Posted answer with id 16','2021-06-24 14:02:16',9),(289,'select','Logged in user with id 1','2021-06-24 14:36:27',1),(290,'select','Logged in user with id 1','2021-06-24 14:37:59',1),(291,'select','Logged in user with id 1','2021-06-24 14:39:04',1),(292,'select','Logged in user with id 1','2021-06-24 14:39:46',1),(293,'select','Logged in user with id 1','2021-06-24 14:40:26',1),(294,'select','Logged in user with id 1','2021-06-24 14:42:46',1),(295,'select','Logged in user with id 1','2021-06-24 14:44:12',1),(296,'select','Logged in user with id 1','2021-06-24 14:45:32',1),(297,'select','Logged in user with id 1','2021-06-24 16:03:05',1),(298,'select','Logged in user with id 5','2021-06-25 10:52:26',5),(299,'select','Logged in user with id 9','2021-06-25 10:52:40',9),(300,'insert','Posted question with id 18','2021-06-25 10:54:41',5),(301,'update','Posted answer with id 17','2021-06-25 10:58:26',9),(302,'select','Logged out user with id 5','2021-06-25 10:58:41',5),(303,'select','Logged in user with id 1','2021-06-25 10:58:43',1),(304,'update','Changed approved from \"0\" to \"1\" of answer with id 17','2021-06-25 10:58:57',1),(305,'update','Changed approved from \"0\" to \"-1\" of answer with id 16','2021-06-25 11:00:32',1),(306,'update','Changed approved from \"0\" to \"1\" of answer with id 14','2021-06-25 11:00:36',1),(307,'select','Logged in user with id 5','2021-06-25 11:03:33',5),(308,'insert','Posted question with id 19','2021-06-25 11:04:25',5),(309,'select','Logged in user with id 9','2021-06-25 11:04:49',9),(310,'update','Posted answer with id 18','2021-06-25 11:05:35',9),(311,'select','Logged out user with id 5','2021-06-25 11:05:56',5),(312,'select','Logged in user with id 1','2021-06-25 11:05:59',1),(313,'select','Logged in user with id 9','2021-06-25 11:07:07',9),(314,'select','Logged in user with id 1','2021-06-25 11:07:09',1),(315,'update','Changed approved from \"0\" to \"1\" of answer with id 18','2021-06-25 11:07:23',1),(316,'select','Logged out user with id 9','2021-06-25 11:07:32',9),(317,'select','Logged in user with id 5','2021-06-25 11:07:38',5),(318,'select','Logged out user with id 5','2021-06-25 11:07:46',5),(319,'select','Logged in user with id 9','2021-06-25 11:07:53',9),(320,'select','Logged in user with id 1','2021-06-25 11:15:00',1),(321,'select','Logged in user with id 1','2021-06-26 18:09:46',1),(322,'update','Changed approved from \"0\" to \"1\" of question with id 17','2021-06-26 18:09:55',1),(323,'select','Logged out user with id 1','2021-06-26 18:18:48',1),(324,'select','Logged in user with id 1','2021-06-28 18:22:12',1),(325,'select','Logged out user with id 1','2021-06-28 18:22:38',1),(326,'update','Changed password of user with id 9','2021-06-30 13:09:19',9),(327,'update','Changed password of user with id 9','2021-06-30 13:10:23',9),(328,'select','Logged in user with id 1','2021-06-30 14:23:26',1),(329,'update','Posted answer with id 19','2021-06-30 15:00:29',1),(330,'select','Logged out user with id 1','2021-06-30 15:00:46',1),(331,'select','Logged in user with id 9','2021-06-30 15:00:56',9),(332,'update','Posted answer with id 20','2021-06-30 15:02:12',9),(333,'insert','Posted question with id 20','2021-06-30 15:07:15',9),(334,'update','Modified user profile info with id 9','2021-06-30 15:28:05',9),(335,'select','Logged in user with id 9','2021-06-30 15:45:12',9),(336,'update','Changed user password with id 9','2021-06-30 15:47:04',9),(337,'update','Changed user password with id 9','2021-06-30 15:47:22',9),(338,'update','Changed user password with id 9','2021-06-30 15:47:30',9),(339,'select','Logged out user with id 9','2021-06-30 16:09:10',9),(340,'select','Logged in user with id 1','2021-06-30 16:09:11',1),(341,'select','Logged in user with id 9','2021-06-30 16:20:32',9),(342,'update','Posted answer with id 21','2021-06-30 16:20:52',9),(343,'update','Posted answer with id 22','2021-06-30 16:24:09',9),(344,'insert','Created category with id 13','2021-06-30 16:31:19',1),(345,'select','Logged in user with id 1','2021-06-30 17:48:21',1),(346,'insert','Sent notification with id 49','2021-06-30 17:49:28',1),(347,'update','Changed account status from \"1\" to \"1\" of user with id 9','2021-06-30 18:03:20',1),(348,'update','Changed roles of user with id 9','2021-06-30 18:06:44',1),(349,'select','Logged in user with id 1','2021-06-30 18:10:05',1),(350,'delete','Deleted category with title \"Advanced Math\"','2021-06-30 18:10:40',1),(351,'select','Logged out user with id 1','2021-06-30 18:19:10',1),(352,'select','Logged in user with id 1','2021-06-30 19:01:22',1),(353,'select','Logged in user with id 1','2021-07-01 20:59:56',1),(354,'select','Logged in user with id 1','2021-07-02 23:19:35',1),(355,'update','Changed account status from \"1\" to \"0\" of user with id 2','2021-07-02 23:33:48',1),(356,'update','Changed account status from \"0\" to \"1\" of user with id 2','2021-07-02 23:33:52',1),(357,'update','Changed roles of user with id 2','2021-07-02 23:34:01',1),(358,'update','Changed roles of user with id 2','2021-07-02 23:34:06',1),(359,'update','Changed roles of user with id 2','2021-07-02 23:34:11',1),(360,'select','Logged out user with id 1','2021-07-02 23:35:14',1),(361,'select','Logged in user with id 1','2021-07-04 21:56:06',1),(362,'select','Logged in user with id 1','2021-07-07 12:52:30',1),(363,'insert','Posted question with id 23','2021-07-07 12:55:54',1),(364,'update','Modified question with id 23','2021-07-07 12:57:18',1),(365,'update','Changed title from \"Math\" to \"Maths\" of category with id 1','2021-07-07 13:01:23',1),(366,'update','Changed title from \"Maths\" to \"Math\" of category with id 1','2021-07-07 13:01:31',1),(367,'update','Changed account status from \"1\" to \"-1\" of user with id 2','2021-07-07 13:04:59',1),(368,'update','Changed account status from \"-1\" to \"1\" of user with id 2','2021-07-07 13:05:04',1),(369,'update','Changed roles of user with id 2','2021-07-07 13:05:14',1),(370,'update','Changed roles of user with id 2','2021-07-07 13:05:20',1),(371,'select','Logged out user with id 1','2021-07-07 13:06:57',1),(372,'select','Logged in user with id 1','2021-07-07 13:07:13',1),(373,'update','Changed account status from \"0\" to \"1\" of user with id 12','2021-07-07 13:07:58',12),(374,'select','Logged in user with id 1','2021-07-08 13:55:20',1),(375,'select','Logged out user with id 1','2021-07-08 13:55:35',1),(376,'update','Changed account status from \"0\" to \"1\" of user with id 14','2021-07-08 14:03:21',14),(377,'select','Logged in user with id 14','2021-07-08 14:03:42',14),(378,'select','Logged out user with id 14','2021-07-08 14:03:49',14),(379,'update','Changed password of user with id 14','2021-07-08 14:04:52',14),(380,'update','Changed account status from \"0\" to \"1\" of user with id 15','2021-07-08 14:13:46',15),(381,'select','Logged in user with id 15','2021-07-08 14:14:07',15),(382,'select','Logged out user with id 15','2021-07-08 14:14:13',15),(383,'update','Changed password of user with id 15','2021-07-08 14:15:54',15),(384,'select','Logged in user with id 15','2021-07-08 14:16:09',15),(385,'select','Logged out user with id 15','2021-07-08 14:19:05',15),(386,'select','Logged in user with id 1','2021-07-08 14:19:20',1),(387,'select','Logged out user with id 1','2021-07-08 14:19:39',1),(388,'select','Logged in user with id 15','2021-07-08 14:19:49',15),(389,'select','Logged out user with id 15','2021-07-08 14:43:13',15),(390,'select','Logged in user with id 1','2021-07-08 14:47:45',1),(391,'select','Logged in user with id 15','2021-07-08 15:04:53',15),(392,'select','Logged out user with id 15','2021-07-08 15:05:15',15),(393,'select','Logged in user with id 15','2021-07-08 15:08:01',15),(394,'update','Modified user profile info with id 15','2021-07-08 15:09:30',15),(395,'select','Logged out user with id 15','2021-07-08 15:55:12',15),(396,'update','Changed account status from \"0\" to \"1\" of user with id 16','2021-07-08 15:57:54',16),(397,'select','Logged in user with id 16','2021-07-08 15:58:19',16),(398,'select','Logged out user with id 16','2021-07-08 15:58:25',16),(399,'update','Changed password of user with id 16','2021-07-08 15:59:20',16),(400,'update','Changed account status from \"0\" to \"1\" of user with id 18','2021-07-08 18:01:26',18),(401,'select','Logged in user with id 18','2021-07-08 18:01:46',18),(402,'select','Logged out user with id 18','2021-07-08 18:01:51',18),(403,'update','Changed password of user with id 18','2021-07-08 18:03:09',18),(404,'update','Changed account status from \"0\" to \"1\" of user with id 19','2021-07-08 18:06:18',19),(405,'select','Logged in user with id 19','2021-07-08 18:06:43',19),(406,'select','Logged out user with id 19','2021-07-08 18:06:48',19),(407,'update','Changed account status from \"0\" to \"1\" of user with id 21','2021-07-08 18:13:26',21),(408,'select','Logged in user with id 21','2021-07-08 18:13:48',21),(409,'select','Logged out user with id 21','2021-07-08 18:13:53',21),(410,'update','Changed password of user with id 21','2021-07-08 18:15:25',21),(411,'select','Logged in user with id 21','2021-07-08 18:15:37',21),(412,'select','Logged out user with id 21','2021-07-08 18:16:39',21),(413,'select','Logged in user with id 21','2021-07-08 18:20:07',21),(414,'update','Modified user profile info with id 21','2021-07-08 18:21:13',21),(415,'update','Modified user profile info with id 21','2021-07-08 18:21:23',21),(416,'update','Changed email from \"negoita.gabi@yahoo.com\" to \"negoita.gabi98@gmail.com\"','2021-07-08 18:22:43',21),(417,'select','Logged out user with id 21','2021-07-08 18:23:08',21),(418,'select','Logged in user with id 21','2021-07-08 18:23:17',21),(419,'update','Changed user password with id 21','2021-07-08 18:24:29',21),(420,'select','Logged out user with id 21','2021-07-08 18:24:33',21),(421,'select','Logged in user with id 21','2021-07-08 18:24:48',21),(422,'update','Posted answer with id 24','2021-07-08 18:25:56',21),(423,'insert','Posted question with id 24','2021-07-08 18:27:38',21),(424,'select','Logged out user with id 21','2021-07-08 18:27:47',21),(425,'select','Logged in user with id 5','2021-07-08 18:30:31',5),(426,'update','Posted answer with id 25','2021-07-08 18:30:52',5),(427,'insert','Posted question with id 25','2021-07-08 18:31:44',5),(428,'select','Logged out user with id 5','2021-07-08 18:31:56',5),(429,'select','Logged in user with id 4','2021-07-08 18:32:24',4),(430,'update','Changed approved from \"0\" to \"-1\" of question with id 24','2021-07-08 18:33:02',4),(431,'update','Changed approved from \"0\" to \"1\" of question with id 20','2021-07-08 18:33:16',4),(432,'update','Changed approved from \"0\" to \"1\" of answer with id 24','2021-07-08 18:33:35',4),(433,'select','Logged out user with id 4','2021-07-08 18:33:48',4),(434,'select','Logged in user with id 21','2021-07-08 18:34:17',21),(435,'update','Modified question with id 24','2021-07-08 18:34:58',21),(436,'select','Logged out user with id 21','2021-07-08 18:35:06',21),(437,'select','Logged in user with id 3','2021-07-08 18:35:32',3),(438,'insert','Created category with id 14','2021-07-08 18:36:33',3),(439,'update','Changed title from \"Advanced Math\" to \"Advanced Maths\" of category with id 14','2021-07-08 18:37:03',3),(440,'delete','Deleted category with title \"Advanced Maths\"','2021-07-08 18:37:11',3),(441,'update','Changed title from \"History\" to \"Histories\" of category with id 7','2021-07-08 18:37:27',3),(442,'update','Changed title from \"Histories\" to \"History\" of category with id 7','2021-07-08 18:37:33',3),(443,'select','Logged out user with id 3','2021-07-08 18:37:36',3),(444,'select','Logged in user with id 2','2021-07-08 18:38:07',2),(445,'insert','Sent notification with id 65','2021-07-08 18:39:09',2),(446,'insert','Sent notification with id 66','2021-07-08 18:39:36',2),(447,'update','Changed account status from \"1\" to \"0\" of user with id 21','2021-07-08 18:41:04',2),(448,'select','Logged out user with id 2','2021-07-08 18:41:23',2),(449,'select','Logged in user with id 2','2021-07-08 18:41:42',2),(450,'update','Changed account status from \"0\" to \"-1\" of user with id 21','2021-07-08 18:42:04',2),(451,'select','Logged out user with id 2','2021-07-08 18:42:12',2),(452,'select','Logged in user with id 2','2021-07-08 18:42:33',2),(453,'update','Changed account status from \"-1\" to \"1\" of user with id 21','2021-07-08 18:42:47',2),(454,'update','Changed roles of user with id 21','2021-07-08 18:42:56',2),(455,'select','Logged out user with id 2','2021-07-08 18:43:05',2),(456,'select','Logged in user with id 21','2021-07-08 18:43:14',21),(457,'select','Logged out user with id 21','2021-07-08 18:43:31',21),(458,'select','Logged in user with id 2','2021-07-08 18:43:36',2),(459,'update','Changed roles of user with id 21','2021-07-08 18:43:53',2),(460,'select','Logged out user with id 2','2021-07-08 18:45:07',2),(461,'select','Logged in user with id 21','2021-07-08 19:57:41',21),(462,'select','Logged in user with id 5','2021-07-12 17:43:07',5),(463,'select','Logged out user with id 5','2021-07-12 17:43:33',5),(464,'select','Logged in user with id 1','2021-07-12 17:46:06',1),(465,'select','Logged out user with id 1','2021-07-12 17:46:16',1),(466,'select','Logged in user with id 1','2021-07-12 18:01:58',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-11 01:22:51',1,'/home/profile/notifications/view-notification/1'),(2,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-11 01:26:21',2,'/home/profile/notifications/view-notification/2'),(3,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 01:28:12',3,'/home/profile/notifications/view-notification/3'),(4,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-11 01:29:35',4,'/home/profile/notifications/view-notification/4'),(5,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-11 01:30:48',5,'/home/profile/notifications/view-notification/5'),(6,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-11 11:52:07',6,'/home/profile/notifications/view-notification/6'),(7,'Your question has been approved',NULL,1,'2021-06-11 12:06:19',6,'/home/questions/1'),(8,'Someone answered your question',NULL,1,'2021-06-11 12:12:06',6,'/home/questions/1'),(9,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-12 22:23:52',7,'/home/profile/notifications/view-notification/9'),(10,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-12 22:24:18',8,'/home/profile/notifications/view-notification/10'),(11,'Someone answered your question',NULL,1,'2021-06-12 22:27:12',1,'/home/questions/3'),(12,'Your answer has been approved',NULL,1,'2021-06-12 22:30:01',7,'/home/questions/3'),(13,'Someone answered your question',NULL,1,'2021-06-12 22:31:38',1,'/home/questions/4'),(14,'Someone answered your question',NULL,1,'2021-06-12 22:33:58',1,'/home/questions/4'),(15,'Someone answered your question',NULL,1,'2021-06-12 22:34:28',1,'/home/questions/3'),(16,'Someone answered your question',NULL,1,'2021-06-12 22:35:28',1,'/home/questions/3'),(17,'Your answer has been approved',NULL,1,'2021-06-12 22:37:20',7,'/home/questions/4'),(18,'Someone answered your question',NULL,1,'2021-06-12 22:37:24',1,'/home/questions/4'),(19,'Your answer has been rejected',NULL,0,'2021-06-12 22:37:51',7,'/home/answers/edit/4'),(20,'Your answer has been rejected',NULL,0,'2021-06-12 22:38:22',7,'/home/answers/edit/6'),(21,'Someone answered your question',NULL,1,'2021-06-12 22:45:37',1,'/home/questions/2'),(22,'Someone answered your question',NULL,0,'2021-06-12 22:54:44',7,'/home/questions/6'),(23,'Someone answered your question',NULL,1,'2021-06-12 22:56:01',8,'/home/questions/8'),(24,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-12 22:57:14',9,'/home/profile/notifications/view-notification/24'),(25,'Someone answered your question',NULL,1,'2021-06-12 22:59:31',8,'/home/questions/9'),(26,'Your answer has been approved',NULL,1,'2021-06-12 22:59:57',9,'/home/questions/9'),(27,'Someone answered your question',NULL,0,'2021-06-14 19:03:47',8,'/home/questions/9'),(28,'Your answer has been rejected',NULL,1,'2021-06-14 21:16:35',9,'/home/answers/edit/12'),(29,'Your answer has been approved',NULL,1,'2021-06-14 21:22:38',9,'/home/questions/9'),(30,'Your answer has been approved',NULL,1,'2021-06-14 21:27:40',9,'/home/questions/9'),(31,'Two more weeks until the summer break!','The summer break is about to come. Hope you enjoyed our platform and we hope you will get back soon!',NULL,'2021-06-15 20:00:22',NULL,'/home/profile/notifications/view-notification/31'),(32,'Someone answered your question',NULL,0,'2021-06-16 18:13:07',8,'/home/questions/9'),(33,'The weekend has come!','Enjoy the weekend by posting and learning new things!',NULL,'2021-06-18 17:04:39',NULL,'/home/profile/notifications/view-notification/33'),(34,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-06-23 15:27:55',10,'/home/profile/notifications/view-notification/34'),(35,'Someone answered your question',NULL,1,'2021-06-23 18:42:58',1,'/home/questions/5'),(36,'Someone answered your question',NULL,1,'2021-06-23 18:43:36',1,'/home/questions/5'),(37,'Your answer has been rejected',NULL,1,'2021-06-23 21:24:55',9,'/home/answers/edit/15'),(38,'Your question has been rejected',NULL,1,'2021-06-24 13:44:34',9,'/home/questions/edit/15'),(39,'Someone answered your question',NULL,1,'2021-06-24 14:02:16',1,'/home/questions/5'),(40,'Someone answered your question',NULL,1,'2021-06-25 10:58:26',5,'/home/questions/18'),(41,'Your answer has been approved',NULL,1,'2021-06-25 10:58:57',9,'/home/questions/18'),(42,'Your answer has been rejected',NULL,1,'2021-06-25 11:00:32',9,'/home/answers/edit/16'),(43,'Your answer has been approved',NULL,1,'2021-06-25 11:00:36',9,'/home/questions/5'),(44,'Your answer has been approved',NULL,1,'2021-06-25 11:07:23',9,'/home/questions/19'),(45,'Someone answered your question',NULL,1,'2021-06-25 11:07:23',5,'/home/questions/19'),(46,'Your question has been approved',NULL,0,'2021-06-26 18:09:55',9,'/home/questions/17'),(47,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-06-30 13:21:29',11,'/home/profile/notifications/view-notification/47'),(48,'Someone answered your question',NULL,1,'2021-06-30 15:00:29',1,'/home/questions/5'),(49,'It\'s vacation time!!!','Enjoy the summer vacation!',NULL,'2021-06-30 17:49:28',NULL,'/home/profile/notifications/view-notification/49'),(50,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-07 12:40:34',12,'/home/profile/notifications/view-notification/50'),(51,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 13:59:44',13,'/home/profile/notifications/view-notification/51'),(52,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 14:02:01',14,'/home/profile/notifications/view-notification/52'),(53,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-07-08 14:12:26',15,'/home/profile/notifications/view-notification/53'),(54,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 15:56:31',16,'/home/profile/notifications/view-notification/54'),(55,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 17:57:00',17,'/home/profile/notifications/view-notification/55'),(56,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 18:00:12',18,'/home/profile/notifications/view-notification/56'),(57,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 18:04:57',19,'/home/profile/notifications/view-notification/57'),(58,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',0,'2021-07-08 18:09:23',20,'/home/profile/notifications/view-notification/58'),(59,'Welcome to Askit!','We hope you will enjoy using our platform and don\'t forget to have fun while doing it!',1,'2021-07-08 18:12:09',21,'/home/profile/notifications/view-notification/59'),(60,'Someone answered your question',NULL,0,'2021-07-08 18:30:52',1,'/home/questions/23'),(61,'Your question has been rejected',NULL,1,'2021-07-08 18:33:02',21,'/home/questions/edit/24'),(62,'Your question has been approved',NULL,0,'2021-07-08 18:33:16',9,'/home/questions/20'),(63,'Your answer has been approved',NULL,1,'2021-07-08 18:33:35',21,'/home/questions/3'),(64,'Someone answered your question',NULL,0,'2021-07-08 18:33:35',1,'/home/questions/3'),(65,'aceasta notificare particulara este doar ca test','nu te speria deoarece aceasta notificare este doar pentru a testa functionalitate de a trimite notificari individuale',1,'2021-07-08 18:39:09',21,'/home/profile/notifications/view-notification/65'),(66,'test test test test test test test test test test test ','test test test test test test test test test test test test test test test test test ',NULL,'2021-07-08 18:39:36',NULL,'/home/profile/notifications/view-notification/66');
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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Aritmetică pt clasa 3/2','<p>Cn știe să facă (234+15-20+5)X2=?</p>','2021-06-11 12:05:27',1,6,1,''),(2,'Ecuatie cu 1 necunoscuta 2x - 1 = 3','<p>Vreau sa rezolv ecuatia&nbsp;</p>\r\n<p>2x - 1 = 3</p>\r\n<p>si nu stiu cum. Imi poate explica cineva?</p>','2021-06-11 12:05:51',1,1,1,NULL),(3,'Primul razboi mondial perioada','<p>Intre ce ani a avut loc primul razboi mondial? Dar al doilea?</p>','2021-06-11 12:27:11',7,1,1,NULL),(4,'Ce inseamna perioada interbelica','<p>Am tot auzit de perioada interbelica dar nu stiu ce inseamna? Cine stie?</p>','2021-06-11 12:28:00',7,1,1,NULL),(5,'ce reprezinta un ip?','<p>cum se defineste un ip in informatica?</p>','2021-06-11 17:08:37',12,1,1,NULL),(6,'Ștefan cel Mare','<p>Intre ce ani a domnit Ștefan cel Mare in Moldova?</p>','2021-06-12 22:48:01',7,7,1,NULL),(7,'Vegan sau raw vegan?      ','<p>Care este diferenta intre regimul vegan si cel raw vegan?</p>','2021-06-12 22:49:36',6,8,1,NULL),(8,'Lac cu nume de animal?','<p>Ce lac din Romania, cu nume de animal cunoasteti?</p>','2021-06-12 22:50:36',8,8,1,NULL),(9,'Ce inseamna un algoritm?','<p>Ce inseamna un algoritm in limbaj informatic?</p>\r\n<p>&nbsp;</p>','2021-06-12 22:56:25',12,8,1,NULL),(10,'Reprezentarea elementelor ce se pot adăuga în interiorul corpului întrebării','<h1>Elemente valabile ce se pot folosi pentru corpul &icirc;ntrebării</h1>\r\n<h2>Imagini</h2>\r\n<p>Aceastea &icirc;și pot modifica caracteristici precum dimensiune sau efecte de culoare.</p>\r\n<p>Se pot adăuga folosind CTRL+V, un link sau fac&acirc;nd upload la o imagine folosind Insert -&gt; Image.</p>\r\n<p><img src=\"http://localhost:9090/uploaded_images/726b42f403b4a0d2c2e472b0255aabaecbf0970fc3bbed53175d3ebb6da7eb0f_1623842502611.png\" alt=\"\" width=\"720\" height=\"172\" /></p>\r\n<p>&nbsp;</p>\r\n<h2>Link-uri</h2>\r\n<p>Se pot adăuga folosind Insert -&gt; Link</p>\r\n<h3><a title=\"Google link\" href=\"http://www.google.com\" target=\"_blank\" rel=\"noopener\">Go to google</a></h3>\r\n<h2>&nbsp;</h2>\r\n<h2>Media</h2>\r\n<p>Aceastea &icirc;și pot modifica caracteristici precum dimensiune.</p>\r\n<p>Se pot adăuga folosind Insert -&gt; Media</p>\r\n<p><iframe src=\"https://www.youtube.com/embed/SGP6Y0Pnhe4\" width=\"571\" height=\"320\" allowfullscreen=\"allowfullscreen\"></iframe></p>\r\n<p>&nbsp;</p>\r\n<h2>Code sample</h2>\r\n<p>Se pot adăuga folosind Insert -&gt; Code sample de unde se poate alege limbajul de programare&nbsp;</p>\r\n<pre class=\"language-markup\"><code>&lt;html&gt;\r\n&lt;head&gt;&lt;/head&gt;\r\n&lt;body&gt;\r\n&lt;p&gt;Hi&lt;/p&gt;\r\n&lt;/body&gt;\r\n&lt;/html&gt;</code></pre>\r\n<p>&nbsp;</p>\r\n<h2>Caractere speciale</h2>\r\n<p>Se pot adăuga folosind Insert -&gt; Special character de unde se poate alege caracterele speciale</p>\r\n<p>₠₦</p>\r\n<p>&nbsp;</p>\r\n<h2>Emoticoane</h2>\r\n<p>Se pot adăuga folosind Insert -&gt; Emoticons de unde se poate alege dintr-o mulține de emoticoane</p>\r\n<p>😁😄</p>\r\n<p>&nbsp;</p>\r\n<h2>Linii orizontale de separare</h2>\r\n<p>Se pot adăuga folosind Insert -&gt; Horizontal line.&nbsp;</p>\r\n<p>Primul paragraf</p>\r\n<hr />\r\n<p>Al doilea paragraf</p>\r\n<p>&nbsp;</p>\r\n<h2>Tabele</h2>\r\n<p>Se pot adăuga folosind Table -&gt; Table de unde se poate alege dimensiunea tabelului</p>\r\n<table style=\"border-collapse: collapse; width: 99.8119%;\" border=\"1\">\r\n<tbody>\r\n<tr>\r\n<td style=\"width: 23.6758%;\">11</td>\r\n<td style=\"width: 23.6758%;\">12</td>\r\n<td style=\"width: 23.6758%;\">13</td>\r\n<td style=\"width: 23.7512%;\">14</td>\r\n</tr>\r\n<tr>\r\n<td style=\"width: 23.6758%;\">21</td>\r\n<td style=\"width: 23.6758%;\">22</td>\r\n<td style=\"width: 23.6758%;\">23</td>\r\n<td style=\"width: 23.7512%;\">24</td>\r\n</tr>\r\n</tbody>\r\n</table>\r\n<p>&nbsp;</p>\r\n<h2>Text cu conținut bogat</h2>\r\n<p>Se pot adăuga folosind Format de unde se poate alege tipul de text, culoare și multe altele</p>\r\n<p><strong>bold text&nbsp;</strong><em>italic</em>&nbsp;<span style=\"text-decoration: underline;\">underline</span>&nbsp;<span style=\"text-decoration: line-through;\">strikethrough</span></p>','2021-06-16 14:21:42',12,1,1,NULL),(11,'Ce reprezinta o celula?','<p>Cum s-ar defini o celula din biologie?</p>','2021-06-16 18:12:14',6,9,0,NULL),(12,'Despre mitocondrie','<p>Ce reprezinta mitocondria si la ce ajuta aceasta?</p>','2021-06-18 16:59:34',6,1,1,NULL),(13,'Picasso: picturi faimoase','<p>Care sunt cele mai populare picturi ale lui Picasso?</p>','2021-06-20 13:02:55',10,9,0,NULL),(14,'Conectarea mai multor calculatoare','<p>Cum se pot conecta mai multe calculatoare fizic? Care sunt instrumentele necesare?</p>','2021-06-23 12:34:19',12,1,1,NULL),(15,'This is just so just ignore it','<p>This is just so just ignore it</p>\r\n<p>This is just so just ignore it</p>\r\n<p>This is just so just ignore it</p>','2021-06-23 18:54:16',10,9,-1,NULL),(16,'Picturi faimoase','<p>Care sunt cele mai faimoase picturi?</p>','2021-06-24 14:01:15',10,9,0,NULL),(17,'Pictori faimosi in intreaga lume','<p>Care sunt cei mai faimosi pictori din lume?</p>','2021-06-24 14:01:43',10,9,1,NULL),(18,'Prezentul continuu in engleza','<p>Cum se formeaza prezentul continuu in engleza + exemple</p>','2021-06-25 10:54:41',2,5,1,NULL),(19,'Prezentul simplu in engleza','<p>Forma prezentului simplu in engleza + exemple</p>','2021-06-25 11:04:25',2,5,1,NULL),(20,'Cum sunt construite intrebarile in engleza','<p>Cum se construieste o intrebare, care e regula si cateva exemple</p>','2021-06-30 15:07:15',2,9,1,NULL),(21,'Ecuație cu 3 necunoscute','Care sunt pașii in rezolvarea unei ecuații cu 3 necunoscute?','2021-06-30 22:36:50',1,2,0,NULL),(22,'Ecuație cu 3 necunoscute','cum se rezolva o ecuație cu 3 necunoscute?','2021-06-30 22:45:30',1,2,0,NULL),(23,'Care e capitala angliei','<p>Care este calitala angliei sau regatului unit?</p>','2021-07-07 12:57:18',8,1,1,NULL),(24,'ce reprezinta mitocondria','<p>ce reprezinta o <strong>mitocondrie&nbsp;</strong>si cum se functioneaza aceasta?</p>','2021-07-08 18:34:58',6,21,0,NULL),(25,'Melodii populare din anul 2021','<p>care sunt cele mai populare melodii ale anului <strong>2021</strong>?</p>\r\n<hr />\r\n<p>Dar din toate timpurile?</p>','2021-07-08 18:31:44',9,5,1,NULL),(26,'cum se construieste viitorul in engleza','care este regula de construire a unei propozitii la viitor?','2021-07-10 00:05:14',2,21,0,NULL),(27,'unde este localizat Parisul?','care este locatia geografica a Parisului?','2021-07-10 00:06:39',8,5,1,NULL),(28,'viitorul in limba engleza','cum se construieste viitorul in limba engleza? exemple?','2021-07-10 00:14:41',2,21,0,NULL),(29,'unde este localizat turnul eiffel','care este locatia geografica a turnului eiffel?','2021-07-10 00:16:01',8,5,1,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_attachment`
--

LOCK TABLES `question_attachment` WRITE;
/*!40000 ALTER TABLE `question_attachment` DISABLE KEYS */;
INSERT INTO `question_attachment` VALUES (1,'uploaded_images/726b42f403b4a0d2c2e472b0255aabaecbf0970fc3bbed53175d3ebb6da7eb0f_1623842502611.png',10),(2,'uploaded_images/489c728a14574573397f8ca5af6bf4b70ae502b6d54818704290ccaefd1138fd_1625758058315.png',24);
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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_vote`
--

LOCK TABLES `question_vote` WRITE;
/*!40000 ALTER TABLE `question_vote` DISABLE KEYS */;
INSERT INTO `question_vote` VALUES (1,1,1,1),(2,1,6,1),(3,1,8,1),(22,-1,9,4),(34,1,6,4),(41,1,9,9),(47,-1,9,1),(48,1,3,1),(49,1,18,1),(54,1,3,21),(55,1,23,5),(57,-1,1,6);
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'superuser','{noop}abcdef123','superuser@email.com',NULL,NULL,'2021-06-11 01:22:40',1),(2,'admin','{noop}abcdef123','admin@email.com',NULL,NULL,'2021-06-11 01:26:11',1),(3,'moderator','{noop}abcdef123','moderator@email.com',NULL,NULL,'2021-06-11 01:28:01',1),(4,'reviewer','{noop}abcdef123','reviewer@email.com','2021-06-01','a','2021-06-11 01:29:24',1),(5,'teacher','{noop}abcdef123','teacher@email.com',NULL,NULL,'2021-06-11 01:30:38',1),(6,'Reizy18','{noop}18112011','raisa.stoica@scoala12galati.ro','2011-11-18','Sunt Raisa. O elevă în clasa 3. Îmi plac unicornii, rozul, sclipiciul și 🌈 curcubeele🦄💖🌸💮🦩🧁🍭🍧🍨ador și dulciurile','2021-06-11 11:51:55',1),(7,'re14mus','{noop}reizi2011','re14mus@yahoo.com',NULL,NULL,'2021-06-12 22:23:40',1),(8,'Roxy','{noop}Reizi2011','Vladescuroxana@yahoo.com',NULL,NULL,'2021-06-12 22:24:08',1),(9,'gabi444','{noop}abcdef123','negoita.gabi3@yahoo.com','1998-03-11','Buna, sunt un student la UGAL, CTI.','2021-06-12 22:57:03',1),(10,'test','{noop}abcdef123','negoita.gabi2@yahoo.com',NULL,NULL,'2021-06-23 15:27:53',1),(11,'test2','{noop}abcdef123','test@email.com',NULL,NULL,'2021-06-30 13:21:18',0),(12,'abc','{noop}abcdef123','negoita.gabi4@yahoo.com',NULL,NULL,'2021-07-07 12:40:22',1),(13,'gabi1','{noop}abcdef123','negoita.gabi1@yahoo.com',NULL,NULL,'2021-07-08 13:59:33',0),(14,'gabi2','{noop}abcdef1234','negoita.gabi98123@gmail.com',NULL,NULL,'2021-07-08 14:01:50',1),(15,'gabi3','{noop}abcdef1234','negoita.gabi123@yahoo.com','1975-05-07','hi there','2021-07-08 14:12:15',1),(16,'gabi','{noop}abcdef1234','negoita.gabi123123123@yahoo.com',NULL,NULL,'2021-07-08 15:56:20',1),(17,'gabi','{noop}abcdef123','negoita.gabi3123@yahoo.com',NULL,NULL,'2021-07-08 17:56:49',0),(18,'gabi','{noop}abcdef1234','negoita.gabi31231241@yahoo.com',NULL,NULL,'2021-07-08 18:00:01',1),(19,'gabi-negoita','{noop}abcdef123','negoita.gabi5643@yahoo.com',NULL,NULL,'2021-07-08 18:04:46',1),(20,'gabi-negoita','{noop}abcdef123','negoita.gabi123124512@yahoo.com',NULL,NULL,'2021-07-08 18:09:12',0),(21,'gabi','{noop}abcdef123','negoita.gabi98@gmail.com','1975-05-06','hi there','2021-07-08 18:11:59',1);
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
INSERT INTO `user_role` VALUES (3,3),(5,1),(6,1),(8,1),(7,1),(4,2),(1,1),(1,2),(1,3),(1,4),(9,1),(2,4);
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

-- Dump completed on 2021-07-12 18:59:59
