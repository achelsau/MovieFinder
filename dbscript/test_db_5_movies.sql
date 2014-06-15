CREATE DATABASE  IF NOT EXISTS `moviefinder` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `moviefinder`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win32 (x86)
--
-- Host: localhost    Database: moviefinder
-- ------------------------------------------------------
-- Server version	5.6.11

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
-- Dumping data for table `moviesource`
--

LOCK TABLES `moviesource` WRITE;
/*!40000 ALTER TABLE `moviesource` DISABLE KEYS */;
INSERT INTO `moviesource` (`id`, `name`, `location`) VALUES (262,'Rotten Tomatoes','http://rottentomatoes.com');
/*!40000 ALTER TABLE `moviesource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `persistentquerytoken`
--

LOCK TABLES `persistentquerytoken` WRITE;
/*!40000 ALTER TABLE `persistentquerytoken` DISABLE KEYS */;
INSERT INTO `persistentquerytoken` (`id`, `token`, `weight`, `parent_query`) VALUES (1,'soldier',1.000000000,8),(2,'hank',1.000000000,8),(3,'tom',1.000000000,8),(4,'lost',1.000000000,9),(5,'hank',1.000000000,9),(6,'tom',1.000000000,9),(7,'island',1.000000000,9),(11,'70s',1.000000000,11),(12,'land',1.000000000,11),(13,'lunar',1.000000000,11),(17,'70s',1.000000000,13),(18,'land',1.000000000,13),(19,'lunar',1.000000000,13),(23,'70s',1.000000000,15),(24,'land',1.000000000,15),(25,'lunar',1.000000000,15),(29,'70s',1.000000000,17),(30,'land',1.000000000,17),(31,'lunar',1.000000000,17),(35,'70s',1.000000000,19),(36,'land',1.000000000,19),(37,'lunar',1.000000000,19),(38,'island',1.000000000,20);
/*!40000 ALTER TABLE `persistentquerytoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `moviedescriptor`
--

LOCK TABLES `moviedescriptor` WRITE;
/*!40000 ALTER TABLE `moviedescriptor` DISABLE KEYS */;
INSERT INTO `moviedescriptor` (`id`, `name`, `year`, `remote_path`, `remote_id`, `alternate_id`, `source`, `synopsis`, `alternate_synopsis`, `image_path`, `release_date`, `rating`) VALUES (1,'Serenity',2005,'http://www.rottentomatoes.com/m/serenity/','12345',NULL,262,'A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.',NULL,'http://ia.media-imdb.com/images/M/MV5BMTI0NTY1MzY4NV5BMl5BanBnXkFtZTcwNTczODAzMQ@@._V1_SY317_CR0,0,214,317_.jpg','2005-09-30',NULL),(2,'Saving Private Ryan',1998,'http://www.rottentomatoes.com/m/saving_private_ryan/','12346',NULL,262,'Steven Spielberg directed this powerful, realistic re-creation of WWII\'s D-day invasion and the immediate aftermath. The story opens with a prologue in which a veteran brings his family to the American cemetery at Normandy, and a flashback then joins Capt. John Miller (Tom Hanks) and GIs in a landing craft making the June 6, 1944, approach to Omaha Beach to face devastating German artillery fire. This mass slaughter of American soldiers is depicted in a compelling, unforgettable 24-minute sequence. Miller\'s men slowly move forward to finally take a concrete pillbox. On the beach littered with bodies is one with the name \'Ryan\' stenciled on his backpack. Army Chief of Staff Gen. George C. Marshall (Harve Presnell), learning that three Ryan brothers from the same family have all been killed in a single week, requests that the surviving brother, Pvt. James Ryan (Matt Damon), be located and brought back to the United States. Capt. Miller gets the assignment, and he chooses a translator, Cpl. Upham (Jeremy Davis), skilled in language but not in combat, to join his squad of right-hand man Sgt. Horvath (Tom Sizemore), plus privates Mellish (Adam Goldberg), Medic Wade (Giovanni Ribisi), cynical Reiben (Edward Burns) from Brooklyn, Italian-American Caparzo (Vin Diesel), and religious Southerner Jackson (Barry Pepper), an ace sharpshooter who calls on the Lord while taking aim. Having previously experienced action in Italy and North Africa, the close-knit squad sets out through areas still thick with Nazis. After they lose one man in a skirmish at a bombed village, some in the group begin to question the logic of losing more lives to save a single soldier. The film\'s historical consultant is Stephen E. Ambrose, and the incident is based on a true occurance in Ambrose\'s 1994 bestseller D-Day: June 6, 1944.',NULL,'http://ia.media-imdb.com/images/M/MV5BNjczODkxNTAxN15BMl5BanBnXkFtZTcwMTcwNjUxMw@@._V1_SY317_CR9,0,214,317_.jpg','1988-11-06',NULL),(3,'Cast Away',2000,'http://www.rottentomatoes.com/m/cast_away/','12347',NULL,262,'An exploration of human survival and the ability of fate to alter even the tidiest of lives with one major event, Cast Away tells the story of Chuck Noland (Tom Hanks), a Federal Express engineer who devotes most of his life to his troubleshooting job. His girlfriend Kelly (Helen Hunt) is often neglected by his dedication to work, and his compulsive personality suggests a conflicted man. But on Christmas Eve, Chuck proposes marriage to Kelly right before embarking on a large assignment. On the assignment, a plane crash strands Chuck on a remote island, and his fast-paced life is slowed to a crawl, as he is miles removed from any human contact. Finding solace only in a volleyball that he befriends, Chuck must now learn to endure the emotional and physical stress of his new life, unsure of when he may return to the civilization he knew before. Cast Away reunites star Hanks with director Robert Zemeckis, their first film together since 1994\'s Oscar-winning Forrest Gump.',NULL,'http://ia.media-imdb.com/images/M/MV5BMTI2MDY0ODEwNF5BMl5BanBnXkFtZTYwMDI2NTk4._V1_SY317_CR5,0,214,317_.jpg','2000-12-22',NULL),(4,'War Horse',2011,'http://www.rottentomatoes.com/m/war_horse/','12348',NULL,262,'Set against a sweeping canvas of rural England and Europe during the First World War, War Horse begins with the remarkable friendship between a horse named Joey and a young man called Albert, who tames and trains him. When they are forcefully parted, the film follows the extraordinary journey of the horse as he moves through the war, changing and inspiring the lives of all those he meets-British cavalry, German soldiers, and a French farmer and his granddaughter-before the story reaches its emotional climax in the heart of No Man\'s Land. The First World War is experienced through the journey of this horse-an odyssey of joy and sorrow, passionate friendship and high adventure.',NULL,'http://ia.media-imdb.com/images/M/MV5BMjExNzkxOTYyNl5BMl5BanBnXkFtZTcwODA0MjU4Ng@@._V1_SY317_CR0,0,214,317_.jpg','2011-02-10',NULL);
/*!40000 ALTER TABLE `moviedescriptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `username`, `password`, `real_name`, `location`, `role`, `is_online`) VALUES (1,'Ion','abcd','abcd','No',NULL,0),(2,'Vasile','abcd','abcd','No',NULL,1),(3,'Florin','abcd','abcd','No',NULL,0),(4,'Lionel','abcd','abcd','No',NULL,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cast_crew_in_movie`
--

LOCK TABLES `cast_crew_in_movie` WRITE;
/*!40000 ALTER TABLE `cast_crew_in_movie` DISABLE KEYS */;
INSERT INTO `cast_crew_in_movie` (`movie_id`, `cast_crew_id`) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(2,19),(2,20),(2,21),(2,22),(2,23),(2,24),(2,25),(2,26),(2,27),(2,28),(2,29),(2,30),(2,31),(2,32),(2,33),(2,15),(2,34),(2,35),(2,17),(2,36),(2,37),(2,38),(2,39),(2,40),(3,41),(3,42),(3,43),(3,44),(3,45),(3,46),(3,27),(3,47),(3,48),(3,49),(3,50),(3,51),(3,52),(3,53),(3,54),(3,55),(3,56),(4,57),(4,58),(4,59),(4,60),(4,32),(4,61),(4,62);
/*!40000 ALTER TABLE `cast_crew_in_movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `persistentquery`
--

LOCK TABLES `persistentquery` WRITE;
/*!40000 ALTER TABLE `persistentquery` DISABLE KEYS */;
INSERT INTO `persistentquery` (`id`, `query_string`, `owner`, `interv`) VALUES (1,'Interstellar mission',1,1000),(2,'Earth seen from moon',1,500),(3,'Solar system',2,1500),(4,'Future Mars exploration',3,2500),(5,'Hypersonic planes',3,2000),(6,'Suborbital intercontinental journey',2,500),(7,'Europa mission 2020',4,500),(8,'tom hanks soldier',1,NULL),(9,'tom hanks lost on an island',1,NULL),(11,'Icarus ship designed by mankind',1,100000),(13,'Icarus ship designed by mankind',1,100000),(15,'Icarus ship designed by mankind',1,100000),(17,'Icarus ship designed by mankind',1,100000),(19,'Icarus ship designed by mankind',1,100000),(20,'alone on an island',1,NULL);
/*!40000 ALTER TABLE `persistentquery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` (`movie_descriptor_id`, `genre_name`) VALUES (1,'37'),(1,'26'),(2,'26'),(2,'7'),(3,'26'),(3,'7'),(4,'26'),(4,'7');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `relevant_movies`
--

LOCK TABLES `relevant_movies` WRITE;
/*!40000 ALTER TABLE `relevant_movies` DISABLE KEYS */;
INSERT INTO `relevant_movies` (`user_id`, `movie_id`) VALUES (1,2);
/*!40000 ALTER TABLE `relevant_movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `moviecrewperson`
--

LOCK TABLES `moviecrewperson` WRITE;
/*!40000 ALTER TABLE `moviecrewperson` DISABLE KEYS */;
INSERT INTO `moviecrewperson` (`id`, `full_name`, `type`) VALUES (1,'Morena Baccarin',0),(2,'Yan Feldman',0),(3,'Alan Tudyk',0),(4,'Tamara Taylor',0),(5,'Sarah Paulson',0),(6,'Summer Glau',0),(7,'Joss Whedon',1),(8,'Sean Maher',0),(9,'Nectar Rose',0),(10,'Chiwetel Ejiofor',0),(11,'Jewel Staite',0),(12,'David Krumholtz',0),(13,'Rafael Feldman',0),(14,'Gina Torres',0),(15,'Nathan Fillion',0),(16,'Michael Hitchcock',0),(17,'Ron Glass',0),(18,'Adam Baldwin',0),(19,'Paul Giamatti',0),(20,'Vin Diesel',0),(21,'Harrison Young',0),(22,'Ted Danson',0),(23,'Bryan Cranston',0),(24,'Dale Dye',0),(25,'Barry Pepper ',0),(26,'Tom Sizemore',0),(27,'Tom Hanks',0),(28,'Jeremy Davies',0),(29,'Matt Damon',0),(30,'Dylan Bruno',0),(31,'Robert Rodat',2),(32,'Steven Spielberg',1),(33,'Edward Burns',0),(34,'Dennis Farina',0),(35,'David Wohl',0),(36,'Max Martini',0),(37,'Harve Presnell',0),(38,'Adam Goldberg',0),(39,'Giovanni Ribisi',0),(40,'Ryan Hurst',0),(41,'Christopher Kriesa',0),(42,'Ashley Edner',0),(43,'Jenifer Lewis',0),(44,'Nick Searcy',0),(45,'Chris Noth',0),(46,'Elden Henson',0),(47,'Michael Forest',0),(48,'Robert Zemeckis',1),(49,'Viveka Davis',0),(50,'Geoffrey Blake',0),(51,'Jay Acovone',0),(52,'Lari White',0),(53,'William Broyles',2),(54,'Valerie Wildman',0),(55,'Nan Martin',0),(56,'Helen Hunt',0),(57,'David Kross',0),(58,'Jeremy Irvine',0),(59,'Emily Watson',0),(60,'Lee Hall',2),(61,'Peter Mullan',0),(62,'David Thewlis',0);
/*!40000 ALTER TABLE `moviecrewperson` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-03-30 15:41:03
