DROP DATABASE IF EXISTS `moviefinder_scalability_test`;
CREATE DATABASE `moviefinder_scalability_test`;
USE `moviefinder_scalability_test`;

-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: moviefinder_test
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
-- Table structure for table `tab_queries`
--

DROP TABLE IF EXISTS `tab_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_queries` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `query_string` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `owner` int(4) NOT NULL,
  `interv` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7402779EA03F4119` (`owner`)
) ENGINE=MyISAM DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci COMMENT='Holds the data related to the the queries the users input';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_relevant_movies`
--

DROP TABLE IF EXISTS `tab_relevant_movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_relevant_movies` (
  `user_id` int(4) unsigned NOT NULL,
  `movie_id` int(5) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci COMMENT='Holds the relevant movies';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_movie_descriptors`
--

DROP TABLE IF EXISTS `tab_movie_descriptors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_movie_descriptors` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT 'Test',
  `remote_path` varchar(255) NOT NULL DEFAULT 'Test',
  `source` int(4) NOT NULL DEFAULT '0',
  `synopsis` text CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci COMMENT='Holds the data related to the the movies';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_movie_sources`
--

DROP TABLE IF EXISTS `tab_movie_sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_movie_sources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_movie_genres`
--

DROP TABLE IF EXISTS `tab_movie_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_movie_genres` (
  `movie_descriptor_id` int(11) NOT NULL,
  `genre_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tab_users`
--

DROP TABLE IF EXISTS `tab_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tab_users` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `real_name` varchar(100) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `is_online` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci COMMENT='Holds the data related to the userbase. ';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-01-06 17:59:50
