-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-10-2018 a las 17:34:10
-- Versión del servidor: 10.1.30-MariaDB
-- Versión de PHP: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `adat_1`
--
CREATE DATABASE IF NOT EXISTS `adat_1` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `adat_1`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coches`
--

DROP TABLE IF EXISTS `coches`;
CREATE TABLE IF NOT EXISTS `coches` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `idBrand` int(11) DEFAULT NULL,
  `modelo` varchar(50) NOT NULL,
  `cavallaje` int(50) NOT NULL,
  `color` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `idBrand` (`idBrand`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `coches`
--

INSERT INTO `coches` (`ID`, `idBrand`, `modelo`, `cavallaje`, `color`) VALUES
(2, 2, 'Mustang', 180, 'Verde'),
(3, 3, 'GranTurismo1', 460, 'Plata'),
(4, 3, 'GranTurismo1', 460, 'Plata');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `marca`
--

DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `idBrand` int(11) NOT NULL AUTO_INCREMENT,
  `brandName` varchar(50) NOT NULL,
  `brandCountry` varchar(50) NOT NULL,
  `brandYearOfFundation` int(11) NOT NULL,
  PRIMARY KEY (`idBrand`),
  UNIQUE KEY `brandName` (`brandName`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `marca`
--

INSERT INTO `marca` (`idBrand`, `brandName`, `brandCountry`, `brandYearOfFundation`) VALUES
(1, 'Ferrari', 'Italia', 1942),
(2, 'Ford', 'E.E.U.U.', 1903),
(3, 'Masserati', 'Italy', 1914);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `coches`
--
ALTER TABLE `coches`
  ADD CONSTRAINT `coches_ibfk_1` FOREIGN KEY (`idBrand`) REFERENCES `marca` (`idBrand`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
