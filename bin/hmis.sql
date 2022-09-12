/*
SQLyog Community v13.1.5  (64 bit)
MySQL - 5.6.20 : Database - hmis
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hmis` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `hmis`;

/*Table structure for table `achievements` */

DROP TABLE IF EXISTS `achievements`;

CREATE TABLE `achievements` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `year` int(4) NOT NULL,
  `branch` bigint(20) DEFAULT NULL,
  `hostel` bigint(20) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  KEY `hostel` (`hostel`),
  CONSTRAINT `achievements_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `achievements_ibfk_2` FOREIGN KEY (`hostel`) REFERENCES `hostel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `achievements` */

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `present_address` text,
  `permanent_address` text,
  `photo` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `national_id` (`national_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `admin` */

/*Table structure for table `attendance` */

DROP TABLE IF EXISTS `attendance`;

CREATE TABLE `attendance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student` bigint(20) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `is_present` tinyint(1) DEFAULT '0',
  `cause_of_absence` text,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  KEY `student` (`student`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`student`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `attendance` */

/*Table structure for table `bazar` */

DROP TABLE IF EXISTS `bazar`;

CREATE TABLE `bazar` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `quantity` double(12,3) NOT NULL,
  `untit_price` double(12,2) NOT NULL,
  `total` double(12,2) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `item_id` (`item_id`),
  KEY `branch` (`branch`),
  CONSTRAINT `bazar_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `bazar_items` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bazar_ibfk_2` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bazar` */

/*Table structure for table `bazar_items` */

DROP TABLE IF EXISTS `bazar_items`;

CREATE TABLE `bazar_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `branch` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `bazar_items_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bazar_items` */

/*Table structure for table `bills` */

DROP TABLE IF EXISTS `bills`;

CREATE TABLE `bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `slip_no` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `bill_to` varchar(255) NOT NULL,
  `pay_method` varchar(255) NOT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `amount` double(12,2) NOT NULL,
  `payment` double(12,2) DEFAULT NULL,
  `due` double(12,2) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bills` */

/*Table structure for table `branch` */

DROP TABLE IF EXISTS `branch`;

CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `hostel` bigint(20) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `cash` bigint(20) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `est_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hostel` (`hostel`),
  CONSTRAINT `branch_ibfk_1` FOREIGN KEY (`hostel`) REFERENCES `hostel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `branch` */

/*Table structure for table `branch_galary` */

DROP TABLE IF EXISTS `branch_galary`;

CREATE TABLE `branch_galary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `photo` mediumblob,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `branch_galary_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `branch_galary` */

/*Table structure for table `cost` */

DROP TABLE IF EXISTS `cost`;

CREATE TABLE `cost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  `amount` double(12,2) NOT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `date` date NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `cost_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `cost` */

/*Table structure for table `deposit` */

DROP TABLE IF EXISTS `deposit`;

CREATE TABLE `deposit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `amount` decimal(16,2) NOT NULL,
  `date` date NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `deposit_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `deposit` */

/*Table structure for table `electricity_bills` */

DROP TABLE IF EXISTS `electricity_bills`;

CREATE TABLE `electricity_bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `meter_no` varchar(255) NOT NULL,
  `units` double(8,4) NOT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `total` double(12,2) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `electricity_bills_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `electricity_bills` */

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `employee_id` varchar(255) DEFAULT NULL,
  `branch` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `present_address` text,
  `permanent_address` text,
  `join_date` date DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `salary` double(12,2) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `national_id` (`national_id`),
  KEY `branch` (`branch`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `employee` */

/*Table structure for table `fees` */

DROP TABLE IF EXISTS `fees`;

CREATE TABLE `fees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `type` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` double(12,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `fees_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `fees` */

/*Table structure for table `food_menu` */

DROP TABLE IF EXISTS `food_menu`;

CREATE TABLE `food_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `days` varchar(255) NOT NULL,
  `breakfast` varchar(255) DEFAULT NULL,
  `lunch` varchar(255) DEFAULT NULL,
  `supper` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `food_menu_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `food_menu` */

/*Table structure for table `hostel` */

DROP TABLE IF EXISTS `hostel`;

CREATE TABLE `hostel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `admin` bigint(20) NOT NULL,
  `slogan` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `cash` bigint(20) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `est_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `slogan` (`slogan`),
  UNIQUE KEY `website` (`website`),
  KEY `admin` (`admin`),
  CONSTRAINT `hostel_ibfk_1` FOREIGN KEY (`admin`) REFERENCES `admin` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `hostel` */

/*Table structure for table `hostel_galary` */

DROP TABLE IF EXISTS `hostel_galary`;

CREATE TABLE `hostel_galary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hostel` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `photo` mediumblob,
  PRIMARY KEY (`id`),
  KEY `hostel` (`hostel`),
  CONSTRAINT `hostel_galary_ibfk_1` FOREIGN KEY (`hostel`) REFERENCES `hostel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `hostel_galary` */

/*Table structure for table `hostel_info` */

DROP TABLE IF EXISTS `hostel_info`;

CREATE TABLE `hostel_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hostel` bigint(20) NOT NULL,
  `history` text,
  `edu_environment` text,
  `administration` text,
  `security` text,
  `entertainment` text,
  `food_info` text,
  `special_facilities` text,
  `instructions` text,
  `facilities` text,
  `galary` text,
  `photo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hostel` (`hostel`),
  CONSTRAINT `hostel_info_ibfk_1` FOREIGN KEY (`hostel`) REFERENCES `hostel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `hostel_info` */

/*Table structure for table `meal` */

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch` bigint(20) NOT NULL,
  `student` bigint(20) NOT NULL,
  `breakfast` int(4) DEFAULT NULL,
  `lunch` int(4) DEFAULT NULL,
  `supper` int(4) DEFAULT NULL,
  `rate` double(7,2) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  KEY `student` (`student`),
  CONSTRAINT `meal_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `meal_ibfk_2` FOREIGN KEY (`student`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `meal` */

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `branch` bigint(20) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `notice` */

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `role` */

insert  into `role`(`id`,`name`) values 
(1,'ADMIN'),
(2,'MANAGER'),
(3,'STUDENT');

/*Table structure for table `room` */

DROP TABLE IF EXISTS `room`;

CREATE TABLE `room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_no` varchar(255) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `seat_count` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `room` */

/*Table structure for table `salary` */

DROP TABLE IF EXISTS `salary`;

CREATE TABLE `salary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee` bigint(20) NOT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `amount` double(12,2) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `employee` (`employee`),
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`employee`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `salary` */

/*Table structure for table `seat` */

DROP TABLE IF EXISTS `seat`;

CREATE TABLE `seat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seat_no` varchar(255) NOT NULL,
  `branch` bigint(20) NOT NULL,
  `room` bigint(20) DEFAULT NULL,
  `monthly_rent` double(12,2) NOT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `student_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `branch` (`branch`),
  KEY `room` (`room`),
  CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `seat_ibfk_2` FOREIGN KEY (`room`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `seat` */

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `name_bangla` varchar(255) DEFAULT NULL,
  `father_name` varchar(255) NOT NULL,
  `mother_name` varchar(255) NOT NULL,
  `parent_occupation` varchar(255) DEFAULT NULL,
  `present_address` text,
  `permanent_address` text,
  `local_guardian` varchar(255) NOT NULL,
  `rel_with_guardian` varchar(255) DEFAULT NULL,
  `guardian_address` text,
  `local_guardian_phone` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ssc_gpa` double(3,2) DEFAULT NULL,
  `ssc_group` varchar(255) DEFAULT NULL,
  `ssc_pass_year` int(4) DEFAULT NULL,
  `school` varchar(255) NOT NULL,
  `present_institute` varchar(255) NOT NULL,
  `class_teacher_name` varchar(255) DEFAULT NULL,
  `class_teacher_phone` varchar(255) DEFAULT NULL,
  `current_subject` varchar(255) NOT NULL,
  `batch_no` varchar(255) NOT NULL,
  `gender` varchar(8) NOT NULL,
  `date_of_birth` date NOT NULL,
  `blood_group` varchar(5) NOT NULL,
  `nationality` varchar(255) NOT NULL,
  `national_id` varchar(255) NOT NULL,
  `passport_no` varchar(255) DEFAULT NULL,
  `father_phone` varchar(255) NOT NULL,
  `mother_phone` varchar(255) DEFAULT NULL,
  `monthly_fee` double(12,2) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `admission_date` date DEFAULT NULL,
  `branch` bigint(20) NOT NULL,
  `seat` bigint(20) NOT NULL,
  `is_active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `national_id` (`national_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `passport_no` (`passport_no`),
  KEY `branch` (`branch`),
  KEY `seat` (`seat`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`branch`) REFERENCES `branch` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`seat`) REFERENCES `seat` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `student` */

/*Table structure for table `student_payment` */

DROP TABLE IF EXISTS `student_payment`;

CREATE TABLE `student_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `voucher_no` varchar(255) DEFAULT NULL,
  `student` bigint(20) NOT NULL,
  `fee_type` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `transaction_no` varchar(255) DEFAULT NULL,
  `month` int(2) NOT NULL,
  `year` int(4) NOT NULL,
  `amount` double(12,2) NOT NULL,
  `paid` double(12,2) DEFAULT NULL,
  `due` double(12,2) DEFAULT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `student` (`student`),
  CONSTRAINT `student_payment_ibfk_1` FOREIGN KEY (`student`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `student_payment` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `verify_code` varchar(255) DEFAULT NULL,
  `code_exp_date` datetime DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user_role` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
