-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: innovation_platform
-- ------------------------------------------------------
-- Server version	5.5.5-10.6.4-MariaDB












--
-- Table structure for table `admitted_email_domains`
--

DROP TABLE IF EXISTS `admitted_email_domains`;

CREATE TABLE `admitted_email_domains` (
  `id` bigint(20) NOT NULL,
  `admitted_email_domains` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `admitted_email_domains` VALUES (1,'elis.org'),(2,'elis.org'),(3,'elis.org'),(3,'company.com'),(4,'elis.org'),(4,'company.com'),(1,'elis.org'),(2,'elis.org'),(3,'elis.org'),(3,'company.com'),(4,'elis.org'),(4,'company.com');

--
-- Table structure for table `challenge_zone`
--

DROP TABLE IF EXISTS `challenge_zone`;

CREATE TABLE `challenge_zone` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `challenge_zone` VALUES (1,'2023-04-17 09:13:26','2023-04-17 09:13:26','AREA PREBOOKING'),(2,'2023-04-17 09:13:31','2023-04-17 09:13:31','AREA EVENTI'),(4,'2023-04-17 09:13:31','2023-04-20 18:27:45','AREA ATTIVAZIONE');

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `text` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creation_user_id` bigint(20) DEFAULT NULL,
  `idea_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `comments` VALUES (1,'2023-04-17 09:14:21','2023-04-20 14:41:33','Wow! :)',1,1),(2,'2023-04-17 09:14:27','2023-04-17 09:14:27','This is a comment',1,2),(26,'2023-04-20 16:08:19','2023-04-20 16:08:19','<p>Bellissima idea! Complimenti!</p>',3,1),(29,'2023-04-21 08:18:47','2023-04-21 08:18:47','<p>Commento da modificare! (è)</p>',3,3),(31,'2023-04-21 09:07:41','2023-04-21 09:07:41','<p>Accenti è ò à ù ì</p>',3,3),(32,'2023-04-21 09:08:19','2023-04-21 09:08:19','<p><span style=\"background-color: rgb(255, 255, 255);

color: rgb(51, 51, 51);

\">👏</span></p>',3,3),(33,'2023-04-26 07:35:19','2023-04-26 07:35:19','This is a comment',1,1),(34,'2023-04-26 07:37:13','2023-04-26 07:37:13','This is a comment 260420223',1,1);

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;

CREATE TABLE `companies` (
  `id` bigint(20) NOT NULL ,
  `date_end_countdown` date DEFAULT NULL,
  `is_home_page_private` bit(1) DEFAULT NULL,
  `max_user_coin_on_same_idea` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_coins` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `companies` VALUES (1,'2023-04-17',0x01,10,'Elis',5,'2023-04-17 09:13:57.000000','2023-04-17 09:13:57.000000'),(2,'2023-04-17',0x01,10,'Elis',5,'2023-04-17 09:13:57.000000','2023-04-17 09:13:57.000000'),(3,'2023-04-17',0x01,5,'Company A',5,'2023-04-17 09:13:57.000000','2023-04-17 09:13:57.000000'),(4,'2023-04-17',0x01,5,'Company A',5,'2023-04-17 09:13:57.000000','2023-04-17 09:13:57.000000');

--
-- Table structure for table `dynamic_config`
--

DROP TABLE IF EXISTS `dynamic_config`;

CREATE TABLE `dynamic_config` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `display_count_down` bit(1) DEFAULT NULL,
  `json_config` tinytext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `json_links_resources` tinytext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `dynamic_config` VALUES (3,'2023-04-17 08:47:14','2023-04-17 08:47:14',0x01,'{}','{}',1),(4,'2023-04-17 08:49:45','2023-04-17 08:49:45',0x01,'{}','{}',2),(5,'2023-04-17 09:11:49','2023-04-17 09:11:49',0x01,'{}','{}',3),(6,'2023-04-17 15:00:22','2023-04-17 15:00:22',0x01,'{}','{}',4);

--
-- Table structure for table `ideas`
--

DROP TABLE IF EXISTS `ideas`;

CREATE TABLE `ideas` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `caption` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_draft` bit(1) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `views` bigint(20) DEFAULT NULL,
  `challenge_zone_id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `creation_user_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `ideas` VALUES (1,'2023-04-17 09:13:57','2023-04-17 09:13:57','Proponiamo una partnership con Lego per fare un modello in scala 1:1 in mattoncini','Idea description',0x00,'C-HR LEGO',0,1,1,1),(2,'2023-04-17 09:14:03','2023-04-17 09:14:03','Erogazione coupon xx€ se nel mese di lancio i clienti, taggano la pagina social nelle IG stories con le pubblicità sparse sul territorio','Description',0x00,'Caccia al tesoro (icone C-HR/banner)',0,2,1,1),(3,'2023-04-18 08:31:36','2023-04-18 08:31:36','Idea caption','Idea description',0x00,'Prova',0,1,1,1),(4,'2023-04-27 10:30:59','2023-04-27 10:30:59','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(5,'2023-04-27 10:31:35','2023-04-27 10:31:35','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(6,'2023-04-27 10:33:06','2023-04-27 10:33:06','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(7,'2023-04-27 10:33:45','2023-04-27 10:33:45','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(8,'2023-04-27 10:34:54','2023-04-27 10:34:54','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(9,'2023-04-27 10:35:20','2023-04-27 10:35:20','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(10,'2023-04-27 10:36:26','2023-04-27 10:36:26','Idea caption','Idea description',0x01,'Idea title',0,1,1,1),(11,'2023-04-27 10:38:00','2023-04-27 10:38:00','Idea caption','Idea description',0x01,'Idea title',0,1,1,1);

--
-- Table structure for table `ideas_users_favourite`
--

DROP TABLE IF EXISTS `ideas_users_favourite`;

CREATE TABLE `ideas_users_favourite` (
  `favourite_ideas_id` bigint(20) NOT NULL,
  `users_favourite_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `ideas_users_favourite` VALUES (3,3),(1,1),(1,3);

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;

CREATE TABLE `password_reset_token` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `password_reset_token_seq`
--

DROP TABLE IF EXISTS `password_reset_token_seq`;

CREATE TABLE `password_reset_token_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `password_reset_token_seq` VALUES (1);

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;

CREATE TABLE `refresh_token` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `expiry_date` datetime NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) DEFAULT NULL) ENGINE=InnoDB ;

INSERT INTO `refresh_token` VALUES (1,'2023-04-17 08:51:17','2023-04-17 08:51:17','2023-04-17 09:51:17','7315f29c-df67-43af-a8aa-949f87a62091',1),(2,'2023-04-17 15:15:22','2023-04-17 15:15:22','2023-04-17 16:15:22','8c64a4c5-4b7b-417a-984f-2988f95c3d96',3),(3,'2023-04-17 15:15:27','2023-04-17 15:15:27','2023-04-17 16:15:27','70d65f9e-5421-478b-9793-e17bbc71032e',3),(4,'2023-04-17 15:15:46','2023-04-17 15:15:46','2023-04-17 16:15:46','5b9a46d8-7e1b-4bc5-9573-d3c9d2cf4ff9',3),(5,'2023-04-17 15:15:56','2023-04-17 15:15:56','2023-04-17 16:15:56','c9ef42e0-ad96-416c-b6bb-b90781a13e94',3),(6,'2023-04-17 15:16:05','2023-04-17 15:16:05','2023-04-17 16:16:05','95bf0503-82c9-40d0-878a-a0b5f991b5ea',3),(7,'2023-04-17 15:45:27','2023-04-17 15:45:27','2023-04-17 16:45:27','1fedc164-f32a-485a-9ba5-38adddf5ecc7',3),(8,'2023-04-17 15:49:05','2023-04-17 15:49:05','2023-04-17 16:49:05','f82b209a-e397-4d28-980e-c2fe598298c3',3),(9,'2023-04-17 16:03:39','2023-04-17 16:03:39','2023-04-17 17:03:39','0f4aba5e-2455-40ea-b07e-055abd53c7f3',3),(10,'2023-04-17 16:14:53','2023-04-17 16:14:53','2023-04-17 17:14:53','5eb6f6c4-dc11-4d3d-9eb9-cce772b72ac0',3),(11,'2023-04-17 16:54:25','2023-04-17 16:54:25','2023-04-17 17:54:25','6747df5d-0a20-47af-9571-dea41716d95e',3),(12,'2023-04-17 20:34:30','2023-04-17 20:34:30','2023-04-17 21:34:30','c9c9a6c1-5663-44c8-a663-95156fb7d2f9',3),(13,'2023-04-17 20:46:06','2023-04-17 20:46:06','2023-04-17 21:46:06','9fb87d5e-ca90-418c-adc0-e212429e27dd',3),(14,'2023-04-17 20:47:37','2023-04-17 20:47:37','2023-04-17 21:47:37','fbd6e192-bc46-4a25-9f35-7658036b09c1',3),(15,'2023-04-17 20:47:55','2023-04-17 20:47:55','2023-04-17 21:47:55','8de85457-1165-4f9b-8f1d-c3f0cd66a52d',3),(16,'2023-04-17 20:48:53','2023-04-17 20:48:53','2023-04-17 21:48:53','c05fbc85-ef3b-457b-ac7b-8d31629f1163',3),(17,'2023-04-17 21:17:24','2023-04-17 21:17:24','2023-04-17 22:17:24','182b20eb-7e4a-4e40-8001-2b3d696d3e5d',3),(18,'2023-04-18 08:28:21','2023-04-18 08:28:21','2023-04-18 09:28:21','6aaf1c06-1f81-4b4f-b635-dc5e5fc8e990',3),(19,'2023-04-19 14:42:38','2023-04-19 14:42:38','2023-04-19 15:42:37','65678e75-46d0-45e0-bf37-007fe4f76e26',3),(20,'2023-04-20 09:38:31','2023-04-20 09:38:31','2023-04-20 10:38:31','4f112a6c-195f-496c-b2ce-3c49c8ab523c',3),(21,'2023-04-20 15:45:19','2023-04-20 15:45:19','2023-04-20 16:45:19','af0c07b1-8013-4676-9482-0abf260978f1',3),(22,'2023-04-20 15:46:33','2023-04-20 15:46:33','2023-04-20 16:46:33','d022aaaf-6a8c-48cb-bb95-9ec2d8cb0b55',3),(23,'2023-04-20 15:59:53','2023-04-20 15:59:53','2023-04-20 16:59:53','76dae5aa-b3e6-47c8-817a-94920b330dfb',3),(24,'2023-04-20 16:01:14','2023-04-20 16:01:14','2023-04-20 17:01:14','3115f731-fb01-4b23-b09f-720220ee7c11',3),(25,'2023-04-20 16:01:59','2023-04-20 16:01:59','2023-04-20 17:01:59','59f368d5-ff70-4662-a643-2801452831ef',3),(26,'2023-04-20 16:02:57','2023-04-20 16:02:57','2023-04-20 17:02:57','560960f8-ebc6-4f64-8732-2e41eea3307c',3),(27,'2023-04-20 16:03:39','2023-04-20 16:03:39','2023-04-20 17:03:39','5c2a55ae-305b-4360-9aa1-652eb41b81ad',3),(28,'2023-04-20 16:04:14','2023-04-20 16:04:14','2023-04-20 17:04:14','0ad6e374-e277-4579-b13e-dc33df9cf1c1',3),(29,'2023-04-20 16:05:00','2023-04-20 16:05:00','2023-04-20 17:05:00','7170e660-d14d-4863-b25b-f88942c136ce',3),(30,'2023-04-20 16:06:34','2023-04-20 16:06:34','2023-04-20 17:06:34','a75fe8e9-9c3c-46f8-af78-b9242bf26f92',3),(31,'2023-04-20 16:06:54','2023-04-20 16:06:54','2023-04-20 17:06:54','edd84a5b-cf53-43da-94f9-e7143a38c1ee',3),(32,'2023-04-20 18:09:22','2023-04-20 18:09:22','2023-04-20 19:09:22','c86cbedb-4194-4dab-a185-9699a3ecbf2b',3);

--
-- Table structure for table `road_map`
--

DROP TABLE IF EXISTS `road_map`;

CREATE TABLE `road_map` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `close_comments_date` date DEFAULT NULL,
  `close_ideas_date` date DEFAULT NULL,
  `close_voting_date` date DEFAULT NULL,
  `open_comments_date` date DEFAULT NULL,
  `open_ideas_date` date DEFAULT NULL,
  `open_voting_date` date DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `road_map` VALUES (1,'2023-04-17 08:45:01','2023-04-26 07:25:02','2024-04-26','2024-04-26','2024-04-26','2023-04-25','2023-04-25','2023-04-25','Roadmap for the next 3 months',1),(4,'2023-04-17 08:49:45','2023-04-17 08:49:45','2024-04-26','2024-04-26','2024-04-26','2023-04-25','2023-04-25','2023-04-25','Elis RoadMap',2),(5,'2023-04-17 09:11:49','2023-04-17 09:11:49','2024-04-26','2024-04-26','2024-04-26','2023-04-25','2023-04-25','2023-04-25','Roadmap for the next 3 months',3),(6,'2023-04-17 15:00:22','2023-04-17 15:00:22','2024-04-26','2024-04-26','2024-04-26','2023-04-25','2023-04-25','2023-04-25','Roadmap for the next 3 months',4);

--
-- Table structure for table `road_map_event`
--

DROP TABLE IF EXISTS `road_map_event`;

CREATE TABLE `road_map_event` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `road_map_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `road_map_event` VALUES (1,'2023-04-17 09:44:13','2023-04-17 09:44:13','Roadmap event description','2023-04-17','2023-04-17','Roadmap event title',1);

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `role` VALUES (1,'2023-04-17 08:45:00','2023-04-17 08:45:00','ROLE_ADMIN'),(2,'2023-04-17 08:45:00','2023-04-17 08:45:00','ROLE_USER'),(3,'2023-04-17 08:45:00','2023-04-17 08:45:00','ROLE_EIH');

--
-- Table structure for table `role_seq`
--

DROP TABLE IF EXISTS `role_seq`;

CREATE TABLE `role_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `role_seq` VALUES (101);

--
-- Table structure for table `subscribed_notifications`
--

DROP TABLE IF EXISTS `subscribed_notifications`;

CREATE TABLE `subscribed_notifications` (
  `id` bigint(20) NOT NULL ,
  `end_of_event` bit(1) DEFAULT NULL,
  `new_comment_on_idea` bit(1) DEFAULT NULL,
  `new_like_on_idea` bit(1) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `subscribed_notifications` VALUES (3,0x01,0x01,NULL,5,NULL,NULL);

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL ,
  `account_non_expired` bit(1) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT NULL,
  `credential_expired` bit(1) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enable` bit(1) DEFAULT NULL,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `used_coins` int(11) DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL) ENGINE=InnoDB ;

INSERT INTO `users` VALUES (1,0x00,0x00,0x00,'admin@elis.org',0x01,'Yuri','Gentili','$2a$10$fTm0z9jUuN5iF6kXFrEM2u0lwCOFC6ji/6zgIFJyMnFRfRU.CekgC',5,'user_admin',1,NULL,NULL),(3,0x00,0x00,0x00,'jon@elis.org',0x01,'John','Doe','$2a$10$pc5oMbitsTeOnneij1F2LeVwaM8QGqqG6a.0YZzcMeHRIim1/si9.',1,'johndoe',1,NULL,NULL),(4,0x00,0x00,0x00,'jsdfson@elis.org',0x01,'Jdfohn','Dosdfe','$2a$10$yZwoFQ8IyLSjdIi6Aoavb.WdhGAXjP0gtm/9hJ7yIxw5zxVm52NRu',0,'josdfhndoe',1,NULL,NULL),(5,0x00,0x00,0x00,'jsdfwerson@elis.org',0x01,'Jwerdfohn','Dowersdfe','$2a$10$bl8hLjSGbx/IHFEyGUvWXu6574srgHmO823yp2BLUzS16PJOKG9ZK',0,'jowersdfhndoe',1,NULL,NULL);

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `users_roles` VALUES (1,1),(1,2),(3,2),(4,2),(5,2),(3,1);

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;

CREATE TABLE `votes` (
  `id` bigint(20) NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `idea_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,) ENGINE=InnoDB ;

INSERT INTO `votes` VALUES (7,'2023-04-17 10:50:19','2023-04-17 10:50:19',1,1),(8,'2023-04-17 10:50:45','2023-04-17 10:50:45',1,1),(217,'2023-04-20 16:36:42','2023-04-20 16:36:42',1,3);

--
-- Dumping routines for database 'innovation_platform'
--










-- Dump completed on 2023-04-27 12:53:33;



ALTER TABLE admitted_email_domains ADD FOREIGN KEY (`id`) REFERENCES `companies` (`id`);
ALTER TABLE challenge_zone MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE challenge_zone ADD PRIMARY KEY (`id`);
ALTER TABLE comments MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE comments ADD PRIMARY KEY (`id`);
ALTER TABLE comments ADD FOREIGN KEY (`creation_user_id`) REFERENCES `users` (`id`);
ALTER TABLE comments ADD FOREIGN KEY (`idea_id`) REFERENCES `ideas` (`id`);
ALTER TABLE companies MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE companies ADD PRIMARY KEY (`id`);
ALTER TABLE dynamic_config MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE dynamic_config ADD PRIMARY KEY (`id`);
ALTER TABLE dynamic_config ADD FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`);
ALTER TABLE ideas MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE ideas ADD PRIMARY KEY (`id`);
ALTER TABLE ideas ADD FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`);
ALTER TABLE ideas ADD FOREIGN KEY (`creation_user_id`) REFERENCES `users` (`id`);
ALTER TABLE ideas ADD FOREIGN KEY (`challenge_zone_id`) REFERENCES `challenge_zone` (`id`);
ALTER TABLE ideas_users_favourite ADD FOREIGN KEY (`favourite_ideas_id`) REFERENCES `ideas` (`id`);
ALTER TABLE ideas_users_favourite ADD FOREIGN KEY (`users_favourite_id`) REFERENCES `users` (`id`);
ALTER TABLE password_reset_token ADD PRIMARY KEY (`id`);
ALTER TABLE password_reset_token ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE refresh_token MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE refresh_token ADD PRIMARY KEY (`id`);
ALTER TABLE refresh_token ADD UNIQUE KEY `refresh_token_UK_r4k4edos30bx9neoq81mdvwph` (`token`);
ALTER TABLE refresh_token ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE road_map MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE road_map ADD PRIMARY KEY (`id`);
ALTER TABLE road_map ADD FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`);
ALTER TABLE road_map_event MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE road_map_event ADD PRIMARY KEY (`id`);
ALTER TABLE road_map_event ADD FOREIGN KEY (`road_map_id`) REFERENCES `road_map` (`id`);
ALTER TABLE role ADD PRIMARY KEY (`id`);
ALTER TABLE subscribed_notifications MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE subscribed_notifications ADD PRIMARY KEY (`id`);
ALTER TABLE subscribed_notifications ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE users MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE users ADD PRIMARY KEY (`id`);
ALTER TABLE users ADD UNIQUE KEY `users_UK_6dotkott2kjsp8vw4d0m25fb7` (`email`);
ALTER TABLE users ADD FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`);
ALTER TABLE users_roles ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE users_roles ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
ALTER TABLE votes MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE votes ADD PRIMARY KEY (`id`);
ALTER TABLE votes ADD FOREIGN KEY (`idea_id`) REFERENCES `ideas` (`id`);
ALTER TABLE votes ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);