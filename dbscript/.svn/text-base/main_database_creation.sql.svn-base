DROP DATABASE IF EXISTS `iragent`;
CREATE DATABASE `iragent`;
USE `iragent`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: iragent
-- ------------------------------------------------------
-- Server version	5.1.66-community

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
-- Table structure for table `tab_query`
--

DROP TABLE IF EXISTS `tab_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_query` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `query_string` varchar(50) NOT NULL,
  `owner` int(4) DEFAULT NULL,
  `interv` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7402779EA03F4119` (`owner`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COMMENT='Holds the data related to the the queries the users input';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_relevant_resources`
--

DROP TABLE IF EXISTS `tab_relevant_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_relevant_resources` (
  `user_id` int(4) unsigned NOT NULL,
  `resource_id` int(5) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Holds the relevant resources between sessions to recalculate the';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_resource_descriptors`
--

DROP TABLE IF EXISTS `tab_resource_descriptors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_resource_descriptors` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(255) NOT NULL,
  `remote_path` varchar(255) DEFAULT NULL,
  `owner` int(4) NOT NULL,
  `resource_description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=117 DEFAULT CHARSET=latin1 COMMENT='Holds the data related to the the resources the users share. ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_resource_owner`
--

DROP TABLE IF EXISTS `tab_resource_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_resource_owner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_users`
--

DROP TABLE IF EXISTS `tab_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_users` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `real_name` varchar(100) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `is_online` tinyint(1) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COMMENT='Holds the data related to the userbase. ';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-24 19:48:13
