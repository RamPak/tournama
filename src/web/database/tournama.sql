-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 02, 2012 at 05:54 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tournama`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_advertise`
--

CREATE TABLE IF NOT EXISTS `tbl_advertise` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `image` varchar(100) NOT NULL,
  `link` varchar(200) NOT NULL,
  `startdate` varchar(10) NOT NULL,
  `enddate` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `tbl_advertise`
--

INSERT INTO `tbl_advertise` (`id`, `name`, `image`, `link`, `startdate`, `enddate`) VALUES
(8, 'محل تبلیغ شما', 'Ad/8.jpg', 'http://www.nojasystem.com/', '1391/05/01', '1391/12/29');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_agency`
--

CREATE TABLE IF NOT EXISTS `tbl_agency` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Tel` varchar(50) NOT NULL,
  `Address` varchar(300) NOT NULL,
  `Logo` text NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UserName` (`UserName`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=21 ;

--
-- Dumping data for table `tbl_agency`
--

INSERT INTO `tbl_agency` (`ID`, `Name`, `Tel`, `Address`, `Logo`, `UserName`, `Password`, `Email`) VALUES
(1, 'مدیر کل', '09122222222', 'تهران', 'logo/1.jpg', 'Administrator', 'noja123456', 'email@gmail.com'),
(2, 'test', 'test', 'test', 'logo/2.jpg', 'test', 'test', 'test@test.com'),
(16, 'آژانس 1', '123', '123', 'logo/16.jpg', 'test1', 'test1', 'test1'),
(17, 'آژانس 2', '123', '123', 'logo/17.jpg', 'test2', 'test2', 'test2'),
(18, 'آژانس 3', '123', '123', 'logo/18.jpg', 'test3', 'test3', 'test3'),
(19, 'آژانس 4', '123', '123', 'logo/19.jpg', 'test4', 'test4', 'test4'),
(20, 'آژانس 5', '123', '123', '', 'test5', 'test5', 'test5');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_city`
--

CREATE TABLE IF NOT EXISTS `tbl_city` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CountryID` int(11) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `userid` int(11) NOT NULL,
  `showinmenu` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

--
-- Dumping data for table `tbl_city`
--

INSERT INTO `tbl_city` (`ID`, `CountryID`, `Name`, `userid`, `showinmenu`) VALUES
(1, 1, 'آمل', 1, 1),
(2, 1, 'تهران', 1, 1),
(3, 1, 'خرمشهر', 1, 0),
(9, 1, 'اصفهان', 1, 0),
(10, 6, 'شهر 1', 1, 1),
(11, 6, 'شهر 2', 1, 0),
(12, 7, 'شهر 3', 1, 1),
(13, 7, 'شهر 4', 1, 0),
(14, 1, 'کیش', 1, 0),
(15, 1, 'قشم', 1, 0),
(16, 1, 'همدان', 1, 0),
(17, 1, 'اهواز', 1, 0),
(18, 1, 'خرمشهر', 1, 0),
(19, 1, 'شیراز', 1, 0),
(20, 1, 'چابهار', 1, 0),
(21, 1, 'ماسوله', 1, 0),
(22, 1, 'بوشهر', 1, 0),
(23, 1, 'رشت', 1, 0),
(24, 1, 'قزوین', 1, 0),
(25, 1, 'خرمدره', 1, 0),
(26, 1, 'سنندج', 1, 0),
(27, 1, 'مشهد', 1, 0),
(28, 1, 'ارومیه', 1, 0),
(29, 1, 'تبریز', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_country`
--

CREATE TABLE IF NOT EXISTS `tbl_country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `userid` int(11) NOT NULL,
  `showinmenu` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=22 ;

--
-- Dumping data for table `tbl_country`
--

INSERT INTO `tbl_country` (`ID`, `Name`, `userid`, `showinmenu`) VALUES
(1, 'ایران', 1, 1),
(6, 'مالزی', 1, 1),
(7, 'تایلند', 1, 1),
(13, 'کشور جدید', 1, 0),
(14, 'کشور جدید', 1, 0),
(15, 'کشور جدید', 1, 0),
(16, 'کشور جدید', 1, 0),
(17, 'کشور جدید', 1, 0),
(18, 'کشور جدید', 1, 0),
(19, 'کشور جدید', 1, 0),
(20, 'کشور جدید', 1, 0),
(21, 'کشور جدید', 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_credit`
--

CREATE TABLE IF NOT EXISTS `tbl_credit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TourID` int(11) NOT NULL,
  `Credit` int(11) NOT NULL,
  `Serial` varchar(255) NOT NULL,
  `Status` bit(1) NOT NULL,
  `date` char(10) NOT NULL,
  `time` char(10) NOT NULL,
  `cardnumber` char(4) NOT NULL,
  `paytype` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tbl_credit`
--

INSERT INTO `tbl_credit` (`ID`, `TourID`, `Credit`, `Serial`, `Status`, `date`, `time`, `cardnumber`, `paytype`) VALUES
(5, 25, 100000, '5678', b'1', '1391/05/02', '12:14', '', 'activation');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hotel`
--

CREATE TABLE IF NOT EXISTS `tbl_hotel` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CityID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

--
-- Dumping data for table `tbl_hotel`
--

INSERT INTO `tbl_hotel` (`ID`, `CityID`, `Name`, `userid`) VALUES
(8, 1, 'هراز', 1),
(9, 1, 'دریا', 1),
(10, 2, 'استقلال', 1),
(11, 2, 'آزادی', 1),
(12, 3, 'شهر', 1),
(13, 3, 'مرکزی', 1),
(14, 9, 'نقش جهان', 1),
(20, 9, 'عباسی', 1),
(16, 10, 'هتل 1', 1),
(17, 11, 'هتل 2', 1),
(18, 13, 'هتل 10', 1),
(19, 12, 'هتل 11', 1),
(21, 1, 'هتل جدید', 1),
(22, 1, 'هتل جدید', 1),
(23, 1, 'هتل جدید', 1),
(24, 1, 'هتل جدید', 1),
(0, -1, '', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_setting`
--

CREATE TABLE IF NOT EXISTS `tbl_setting` (
  `clickprice` decimal(12,0) NOT NULL,
  `activationprice` decimal(12,0) NOT NULL,
  `SystemName` varchar(50) NOT NULL,
  `ActivationStatus` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbl_setting`
--

INSERT INTO `tbl_setting` (`clickprice`, `activationprice`, `SystemName`, `ActivationStatus`) VALUES
('100', '100000', 'نوژا تور', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_tour`
--

CREATE TABLE IF NOT EXISTS `tbl_tour` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AgencyID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `SourceID` int(11) NOT NULL,
  `DestinationID` int(11) NOT NULL,
  `HotelID` int(11) NOT NULL,
  `Credit` decimal(15,0) NOT NULL,
  `StartPrice` decimal(15,0) DEFAULT NULL,
  `Date` varchar(10) DEFAULT NULL,
  `Description` varchar(1000) DEFAULT NULL,
  `Duration` varchar(50) DEFAULT NULL,
  `GroupID` int(11) NOT NULL,
  `On_Sale` int(1) NOT NULL,
  `enable` int(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

--
-- Dumping data for table `tbl_tour`
--

INSERT INTO `tbl_tour` (`ID`, `AgencyID`, `Name`, `SourceID`, `DestinationID`, `HotelID`, `Credit`, `StartPrice`, `Date`, `Description`, `Duration`, `GroupID`, `On_Sale`, `enable`) VALUES
(6, 16, 'تابستان امل', 2, 29, 0, '0', '120000', '1391/05/30', '456546456456', '3 روز', 2, 1, 1),
(7, 16, 'تهران گردی', 1, 28, 0, '0', '1100000', '1391/04/13', '', '2 روز', 2, 0, 1),
(8, 17, 'مناطق جنگی', 2, 27, 0, '0', '510000', '1391/04/29', '456456456456', '10 روز', 2, 1, 1),
(9, 17, 'جاذبه های نصف جهان', 2, 26, 0, '0', '1500000', '1391/04/28', '456456546', '5 روز', 2, 0, 1),
(26, 1, 'tttt', 1, 10, 16, '0', '120000', '1391/05/12', 'بلابلا', '4', 2, 0, 0),
(10, 18, 'ساحل مالزی', 2, 25, 0, '0', '30000000', '1391/04/27', '456546456546', '12 روز', 3, 1, 1),
(11, 18, 'جنگل های مالزی', 9, 24, 0, '0', '14200000', '1391/04/26', '', '20 روز', 3, 0, 1),
(12, 19, 'برج های تایلند', 1, 23, 0, '0', '2530000', '1391/04/25', '456456456456', '5 روز', 3, 1, 1),
(13, 19, 'موج سواری تایلند', 3, 21, 0, '0', '1220000', '1391/04/24', 'موج سواری جلب', '13 روز', 3, 0, 1),
(25, 1, '456', 1, 2, 10, '0', '55555', '1391/05/07', '', '89', 2, 0, 1),
(14, 20, 'نمک ابرود', 2, 20, 0, '0', '100000', '1391/04/23', '4564566456456', '1 روز', 4, 0, 1),
(15, 2, 'تست', 2, 19, 0, '0', '111000', '1391/04/28', '', '3', 2, 0, 1),
(16, 16, 'تست', 2, 18, 0, '0', '20000', '1391/04/28', '', '2', 2, 0, 1),
(17, 17, 'test2', 1, 17, 0, '0', '12000000', '1391/04/07', '', 'دو روز و سه شب', 2, 0, 1),
(18, 18, 'test3', 1, 16, 0, '0', '156156', '1391/06/04', '', '123', 2, 0, 1),
(19, 19, 'test4', 1, 15, 0, '0', '4168', '1391/04/05', '', 'sdf', 2, 0, 1),
(20, 20, 'test5', 1, 14, 0, '0', '49155', '1391/04/14', '', '54', 2, 0, 1),
(21, 16, 'test6', 1, 9, 0, '0', '186', '1391/04/05', '', '4325', 2, 0, 1),
(22, 17, 'test7', 1, 9, 0, '0', '4355', '1392/05/20', '', '456', 2, 0, 1),
(23, 18, 'test8', 1, 1, 0, '0', '18979', '1391/04/21', '', '2343', 2, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_tourgroup`
--

CREATE TABLE IF NOT EXISTS `tbl_tourgroup` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tbl_tourgroup`
--

INSERT INTO `tbl_tourgroup` (`ID`, `Name`) VALUES
(2, 'تورهای داخلی'),
(3, 'تورهای خارجی'),
(4, 'تورهای یکروزه');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
