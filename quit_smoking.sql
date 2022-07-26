-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 26, 2022 at 04:59 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quit_smoking`
--

-- --------------------------------------------------------

--
-- Table structure for table `achievement`
--

CREATE TABLE `achievement` (
  `achievement_id` int(10) NOT NULL,
  `title` varchar(50) CHARACTER SET latin1 NOT NULL,
  `hint` varchar(500) CHARACTER SET latin1 NOT NULL,
  `reward` varchar(50) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `community_forum`
--

CREATE TABLE `community_forum` (
  `forum_id` int(10) UNSIGNED NOT NULL,
  `title` varchar(50) NOT NULL,
  `content` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `community_forum`
--

INSERT INTO `community_forum` (`forum_id`, `title`, `content`) VALUES
(1, 'forum title 1', 'forum content 1'),
(2, 'forum title 2', 'forum content 2'),
(3, 'forum title 3', 'forum content 3'),
(4, 'forum title 4', 'forum content 4'),
(5, 'forum title 5', 'forum content 5');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `setting_id` int(10) UNSIGNED NOT NULL,
  `setting_type` varchar(20) NOT NULL,
  `setting_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`setting_id`, `setting_type`, `setting_content`) VALUES
(1, 'smoking_tips', '{\"title\":\"sample tips title\",\"content\":\"sample tips content\"}'),
(2, 'notification', '{\"title\": \"sample234\", \"content\":\"content2\"}'),
(3, 'pro_plan', '{\"price\":\"6.66\"}'),
(4, 'community', '{\"fb_grp_url\": \"www.facebook.comm\"}');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(10) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) CHARACTER SET latin1 NOT NULL,
  `password` varchar(32) CHARACTER SET latin1 NOT NULL,
  `pro` int(10) NOT NULL,
  `amt_cigarette` int(10) NOT NULL,
  `price_cigarette` double NOT NULL,
  `created_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `name`, `email`, `password`, `pro`, `amt_cigarette`, `price_cigarette`, `created_time`) VALUES
(1, 'Mickey Mouse', 'mickeymouse@gmail.com', 'Mickey123!@', 2, 5, 10, '2022-07-23 17:07:46'),
(12, 'joey', 'chengxinye@gmail.com', 'Joey1234!@', 1, 5, 5, '2022-07-26 19:38:47');

-- --------------------------------------------------------

--
-- Table structure for table `userachievement`
--

CREATE TABLE `userachievement` (
  `user_id` int(10) NOT NULL,
  `achievement_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `userachievement`
--

INSERT INTO `userachievement` (`user_id`, `achievement_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(3, 1),
(3, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `achievement`
--
ALTER TABLE `achievement`
  ADD PRIMARY KEY (`achievement_id`);

--
-- Indexes for table `community_forum`
--
ALTER TABLE `community_forum`
  ADD PRIMARY KEY (`forum_id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`setting_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `achievement`
--
ALTER TABLE `achievement`
  MODIFY `achievement_id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `community_forum`
--
ALTER TABLE `community_forum`
  MODIFY `forum_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
  MODIFY `setting_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
