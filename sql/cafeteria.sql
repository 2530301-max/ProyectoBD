-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-11-2026 a las 17:46:11
-- Versión del servidor: 10.11.16-MariaDB
-- Versión de PHP: 8.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cafeteria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`) VALUES
(5, 'bebidas alcoholicas'),
(1, 'Bebidas calientes'),
(2, 'Bebidas frias'),
(4, 'Jabones'),
(3, 'Reposteria');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ciudades`
--

CREATE TABLE `ciudades` (
  `id_ciudad` int(11) NOT NULL,
  `nombre` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `ciudades`
--

INSERT INTO `ciudades` (`id_ciudad`, `nombre`) VALUES
(1, 'CDMX'),
(2, 'Guadalajara'),
(3, 'León'),
(5, 'Mérida'),
(4, 'Monterrey'),
(6, 'Puebla'),
(7, 'Querétaro'),
(8, 'Toluca');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `id_ciudad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `email`, `id_ciudad`) VALUES
(1, 'Carlos Ruiz', 'carlos@mail.com', 4),
(2, 'Laura Solares', 'laura@mail.com', 2),
(3, 'Roberto Díaz', 'roberto@mail.com', 4),
(4, 'Mariana Torres', 'mariana@mail.com', 1),
(5, 'Jorge Fernández', 'jorge@mail.com', 6),
(6, 'Sofía Mendoza', 'sofia@mail.com', 7),
(7, 'Andrea Castro', 'andrea@mail.com', 5),
(8, 'Gabriel Lima', 'gabriel@mail.com', 4),
(9, 'Patricia Vega', 'patricia@mail.com', 8),
(10, 'Fernando Ramos', 'fernando@mail.com', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

CREATE TABLE `detalle_venta` (
  `id_venta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL CHECK (`cantidad` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `detalle_venta`
--

INSERT INTO `detalle_venta` (`id_venta`, `id_producto`, `cantidad`) VALUES
(1001, 3, 2),
(1001, 12, 1),
(1002, 12, 3),
(1003, 2, 1),
(1003, 11, 2),
(1004, 5, 1),
(1004, 13, 1),
(1005, 1, 1),
(1005, 14, 1),
(1006, 7, 2),
(1006, 12, 2),
(1007, 3, 1),
(1008, 6, 1),
(1008, 10, 2),
(1009, 2, 2),
(1010, 4, 1),
(1010, 15, 2),
(1011, 8, 1),
(1011, 16, 1),
(1012, 3, 3),
(1012, 14, 2),
(1013, 7, 1),
(1013, 11, 1),
(1014, 6, 2),
(1015, 1, 2),
(1015, 13, 2),
(1016, 12, 4),
(1017, 3, 1),
(1017, 9, 1),
(1018, 2, 1),
(1018, 16, 1),
(1019, 5, 2),
(1019, 10, 1),
(1020, 7, 1),
(1020, 11, 1),
(1020, 12, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_categoria` int(11) NOT NULL,
  `precio_referencia` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `nombre`, `id_categoria`, `precio_referencia`) VALUES
(1, 'Americano', 1, 50.00),
(2, 'Capuchino Grande', 1, 65.00),
(3, 'Espresso', 1, 45.00),
(4, 'Té Chai Latte', 1, 45.00),
(5, 'Té Verde', 1, 50.00),
(6, 'Frappé de Caramel', 2, 75.00),
(7, 'Latte Frío', 2, 60.00),
(8, 'Té Frío', 2, 55.00),
(9, 'Cheesecake', 3, 75.00),
(10, 'Dona de Chocolate', 3, 30.00),
(11, 'Galleta de Chispas', 3, 30.00),
(12, 'Muffin de Arándanos', 3, 45.00),
(13, 'Bagel de Queso', 2, 45.00),
(14, 'Croissant de Mantequilla', 4, 45.00),
(15, 'Pan de Dulce', 4, 35.00),
(16, 'Sandwich Pavo', 5, 105.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vendedores`
--

CREATE TABLE `vendedores` (
  `id_vendedor` varchar(10) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `vendedores`
--

INSERT INTO `vendedores` (`id_vendedor`, `nombre`) VALUES
('V-01', 'Ana Gómez'),
('V-02', 'Pedro Páramo'),
('V-03', 'Luisa Fernández');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id_venta` int(11) NOT NULL,
  `fecha_venta` date NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_vendedor` varchar(10) NOT NULL,
  `total_venta` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `ventas`
--

INSERT INTO `ventas` (`id_venta`, `fecha_venta`, `id_cliente`, `id_vendedor`, `total_venta`) VALUES
(1001, '2026-10-15', 1, 'V-01', 175.00),
(1002, '2026-10-15', 2, 'V-02', 135.00),
(1003, '2026-10-16', 1, 'V-01', 125.00),
(1004, '2026-10-16', 3, 'V-01', 100.00),
(1005, '2026-10-16', 4, 'V-03', 95.00),
(1006, '2026-10-17', 2, 'V-02', 210.00),
(1007, '2026-10-17', 5, 'V-01', 45.00),
(1008, '2026-10-17', 6, 'V-03', 140.00),
(1009, '2026-10-18', 3, 'V-01', 130.00),
(1010, '2026-10-18', 7, 'V-02', 115.00),
(1011, '2026-10-18', 1, 'V-03', 160.00),
(1012, '2026-10-19', 8, 'V-01', 225.00),
(1013, '2026-10-19', 4, 'V-03', 95.00),
(1014, '2026-10-19', 9, 'V-02', 150.00),
(1015, '2026-10-19', 6, 'V-01', 190.00),
(1016, '2026-10-19', 5, 'V-02', 180.00),
(1017, '2026-10-19', 7, 'V-03', 120.00),
(1018, '2026-10-19', 10, 'V-01', 170.00),
(1019, '2026-10-19', 2, 'V-02', 110.00),
(1020, '2026-10-19', 8, 'V-03', 145.00),
(1021, '2026-11-17', 3, 'V-02', 200.00);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_verificacion_totales`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_verificacion_totales` (
`id_venta` int(11)
,`total_excel` decimal(10,2)
,`total_calculado` decimal(42,2)
,`diferencia` decimal(43,2)
);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `ciudades`
--
ALTER TABLE `ciudades`
  ADD PRIMARY KEY (`id_ciudad`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_cliente_ciudad` (`id_ciudad`);

--
-- Indices de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD PRIMARY KEY (`id_venta`,`id_producto`),
  ADD KEY `fk_det_producto` (`id_producto`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `fk_producto_categoria` (`id_categoria`);

--
-- Indices de la tabla `vendedores`
--
ALTER TABLE `vendedores`
  ADD PRIMARY KEY (`id_vendedor`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id_venta`),
  ADD KEY `fk_venta_cliente` (`id_cliente`),
  ADD KEY `fk_venta_vendedor` (`id_vendedor`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `ciudades`
--
ALTER TABLE `ciudades`
  MODIFY `id_ciudad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id_venta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1022;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_verificacion_totales`
--
DROP TABLE IF EXISTS `v_verificacion_totales`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_verificacion_totales`  AS SELECT `v`.`id_venta` AS `id_venta`, `v`.`total_venta` AS `total_excel`, sum(`d`.`cantidad` * `p`.`precio_referencia`) AS `total_calculado`, `v`.`total_venta`- sum(`d`.`cantidad` * `p`.`precio_referencia`) AS `diferencia` FROM ((`ventas` `v` join `detalle_venta` `d` on(`d`.`id_venta` = `v`.`id_venta`)) join `productos` `p` on(`p`.`id_producto` = `d`.`id_producto`)) GROUP BY `v`.`id_venta`, `v`.`total_venta` ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `fk_cliente_ciudad` FOREIGN KEY (`id_ciudad`) REFERENCES `ciudades` (`id_ciudad`);

--
-- Filtros para la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD CONSTRAINT `fk_det_producto` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  ADD CONSTRAINT `fk_det_venta` FOREIGN KEY (`id_venta`) REFERENCES `ventas` (`id_venta`) ON DELETE CASCADE;

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`);

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_venta_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  ADD CONSTRAINT `fk_venta_vendedor` FOREIGN KEY (`id_vendedor`) REFERENCES `vendedores` (`id_vendedor`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
