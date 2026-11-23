package io.snake.com;

import io.snake.com.config.ConexionDB;
import io.snake.com.dao.CategoriaDAO;
import io.snake.com.dao.ProductoDAO;
import io.snake.com.dao.VentaDAO;
import io.snake.com.dto.CategoriaDTO;
import io.snake.com.dto.ProductoDTO;
import io.snake.com.dto.VentaDTO;
import io.snake.com.ui.CafeteriaFX;
import javafx.application.Application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final CategoriaDAO categoriaDao = new CategoriaDAO();
    private static final VentaDAO ventaDao = new VentaDAO();
    private static final ProductoDAO productoDao = new ProductoDAO();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 0 && "--consola".equalsIgnoreCase(args[0])) {
            ejecutarModoConsola();
            return;
        }
        Application.launch(CafeteriaFX.class, args);
    }

    public static void ejecutarModoConsola() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            switch (opcion) {
                case 1 -> crearCategoria();
                case 2 -> listarCategorias();
                case 3 -> buscarCategoria();
                case 4 -> actualizarCategoria();
                case 5 -> eliminarCategoria();
                case 6 -> registrarVenta();
                case 7 -> listarVentas();
                case 8 -> buscarVenta();
                case 9 -> actualizarVenta();
                case 10 -> eliminarVenta();
                case 11 -> crearProducto();
                case 12 -> listarProductos();
                case 13 -> buscarProducto();
                case 14 -> actualizarProducto();
                case 15 -> eliminarProducto();
                case 0 -> System.out.println("Hasta luego.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        ConexionDB.cerrarSesion();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL (Usuario: " + ConexionDB.getUsuario() + ") ===");
        System.out.println("--- CATEGORÍAS ---");
        System.out.println("1. Crear Categoría");
        System.out.println("2. Listar Categorías");
        System.out.println("3. Buscar Categoría por ID");
        System.out.println("4. Actualizar Categoría");
        System.out.println("5. Eliminar Categoría");
        System.out.println("--- VENTAS ---");
        System.out.println("6. Registrar Venta");
        System.out.println("7. Listar Ventas");
        System.out.println("8. Buscar Venta por ID");
        System.out.println("9. Actualizar Venta");
        System.out.println("10. Eliminar Venta");
        System.out.println("--- PRODUCTOS ---");
        System.out.println("11. Crear Producto");
        System.out.println("12. Listar Productos");
        System.out.println("13. Buscar Producto por ID");
        System.out.println("14. Actualizar Producto");
        System.out.println("15. Eliminar Producto");
        System.out.println("0. Salir");
    }

    // ---------- MÉTODOS DE CATEGORÍAS ----------

    private static void crearCategoria() {
        String nombre = leerTexto("Nombre de la nueva categoría: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        try {
            CategoriaDTO c = categoriaDao.crear(new CategoriaDTO(nombre));
            System.out.println("Categoría creada con ID " + c.getId());
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void listarCategorias() {
        try {
            List<CategoriaDTO> lista = categoriaDao.listar();
            if (lista.isEmpty()) {
                System.out.println("No hay categorías registradas.");
                return;
            }
            System.out.println("\nID    NOMBRE");
            System.out.println("-----------------------------");
            lista.forEach(System.out::println);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void buscarCategoria() {
        int id = leerEntero("ID a buscar: ");
        try {
            CategoriaDTO c = categoriaDao.buscarPorId(id);
            System.out.println(c != null ? c : "No existe una categoría con ID " + id);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void actualizarCategoria() {
        int id = leerEntero("ID a actualizar: ");
        try {
            CategoriaDTO c = categoriaDao.buscarPorId(id);
            if (c == null) {
                System.out.println("No existe una categoría con ID " + id);
                return;
            }
            System.out.println("Nombre actual: " + c.getNombre());
            String nuevo = leerTexto("Nuevo nombre: ");
            if (nuevo.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }
            c.setNombre(nuevo);
            System.out.println(categoriaDao.actualizar(c) ? "Categoría actualizada." : "No se realizaron cambios.");
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void eliminarCategoria() {
        int id = leerEntero("ID a eliminar: ");
        try {
            CategoriaDTO c = categoriaDao.buscarPorId(id);
            if (c == null) {
                System.out.println("No existe una categoría con ID " + id);
                return;
            }
            String conf = leerTexto("¿Eliminar \"" + c.getNombre() + "\"? (s/n): ");
            if (conf.equalsIgnoreCase("s")) {
                System.out.println(categoriaDao.eliminar(id) ? "Categoría eliminada." : "No se pudo eliminar.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    // ---------- MÉTODOS DE VENTAS ----------

    private static void registrarVenta() {
        try {
            LocalDate fecha = LocalDate.now();
            int idCliente = leerEntero("ID del cliente: ");
            String idVendedor = leerTexto("ID del vendedor (ej. V-01): ");
            double total = leerDecimal("Total de la venta: ");

            VentaDTO nuevaVenta = new VentaDTO(fecha, idCliente, idVendedor, total);
            VentaDTO creada = ventaDao.crear(nuevaVenta);
            System.out.println("¡Venta registrada exitosamente con el ID: " + creada.getIdVenta() + "!");
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void listarVentas() {
        try {
            List<VentaDTO> lista = ventaDao.listar();
            if (lista.isEmpty()) {
                System.out.println("No hay ventas registradas.");
                return;
            }
            System.out.println("\n--- LISTADO DE VENTAS ---");
            lista.forEach(System.out::println);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void buscarVenta() {
        int id = leerEntero("Ingresa el ID de la venta a buscar: ");
        try {
            VentaDTO v = ventaDao.buscarPorId(id);
            System.out.println(v != null ? v : "No existe una venta con el ID " + id);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void actualizarVenta() {
        int id = leerEntero("ID de la venta a actualizar: ");
        try {
            VentaDTO v = ventaDao.buscarPorId(id);
            if (v == null) {
                System.out.println("No existe una venta con ese ID.");
                return;
            }
            System.out.println("Venta actual: " + v);

            int nuevoCliente = leerEntero("Nuevo ID de cliente (" + v.getIdCliente() + "): ");
            String nuevoVendedor = leerTexto("Nuevo ID de vendedor (" + v.getIdVendedor() + "): ");
            double nuevoTotal = leerDecimal("Nuevo total (" + v.getTotalVenta() + "): ");

            v.setIdCliente(nuevoCliente);
            v.setIdVendedor(nuevoVendedor);
            v.setTotalVenta(nuevoTotal);

            boolean actualizado = ventaDao.actualizar(v);
            System.out.println(actualizado ? "Venta actualizada correctamente." : "No se pudo actualizar la venta.");
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void eliminarVenta() {
        int id = leerEntero("ID de la venta a eliminar: ");
        try {
            VentaDTO v = ventaDao.buscarPorId(id);
            if (v == null) {
                System.out.println("No existe una venta con ese ID.");
                return;
            }
            String confirmacion = leerTexto("¿Estás seguro de eliminar la venta " + id + "? (s/n): ");
            if (confirmacion.equalsIgnoreCase("s")) {
                boolean eliminado = ventaDao.eliminar(id);
                System.out.println(eliminado ? "Venta eliminada con éxito." : "No se pudo eliminar.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    // ---------- MÉTODOS DE PRODUCTOS ----------

    private static void crearProducto() {
        String nombre = leerTexto("Nombre del nuevo producto: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }
        int idCategoria = leerEntero("ID de la categoría a la que pertenece: ");
        double precio = leerDecimal("Precio de referencia: ");

        try {
            ProductoDTO nuevo = new ProductoDTO(nombre, idCategoria, precio);
            ProductoDTO creado = productoDao.crear(nuevo);
            System.out.println("¡Producto creado con éxito con el ID: " + creado.getIdProducto() + "!");
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void listarProductos() {
        try {
            List<ProductoDTO> lista = productoDao.listar();
            if (lista.isEmpty()) {
                System.out.println("No hay productos registrados.");
                return;
            }
            System.out.println("\n--- LISTADO DE PRODUCTOS ---");
            lista.forEach(System.out::println);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void buscarProducto() {
        int id = leerEntero("Ingresa el ID del producto a buscar: ");
        try {
            ProductoDTO p = productoDao.buscarPorId(id);
            System.out.println(p != null ? p : "No existe un producto con el ID " + id);
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void actualizarProducto() {
        int id = leerEntero("ID del producto a actualizar: ");
        try {
            ProductoDTO p = productoDao.buscarPorId(id);
            if (p == null) {
                System.out.println("No existe un producto con ese ID.");
                return;
            }
            System.out.println("Producto actual: " + p);

            String nuevoNombre = leerTexto("Nuevo nombre (" + p.getNombre() + "): ");
            if (!nuevoNombre.isEmpty()) {
                p.setNombre(nuevoNombre);
            }

            int nuevaCat = leerEntero("Nuevo ID de categoría (" + p.getIdCategoria() + "): ");
            p.setIdCategoria(nuevaCat);

            double nuevoPrecio = leerDecimal("Nuevo precio (" + p.getPrecioReferencia() + "): ");
            p.setPrecioReferencia(nuevoPrecio);

            boolean actualizado = productoDao.actualizar(p);
            System.out.println(actualizado ? "Producto actualizado correctamente." : "No se realizaron cambios.");
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    private static void eliminarProducto() {
        int id = leerEntero("ID del producto a eliminar: ");
        try {
            ProductoDTO p = productoDao.buscarPorId(id);
            if (p == null) {
                System.out.println("No existe un producto con ese ID.");
                return;
            }
            String confirmacion = leerTexto("¿Estás seguro de eliminar el producto \"" + p.getNombre() + "\"? (s/n): ");
            if (confirmacion.equalsIgnoreCase("s")) {
                boolean eliminado = productoDao.eliminar(id);
                System.out.println(eliminado ? "Producto eliminado con éxito." : "No se pudo eliminar.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (SQLException e) {
            manejarError(e);
        }
    }

    // ---------- Manejo de Excepciones ----------

    private static void manejarError(SQLException e) {
        switch (e.getErrorCode()) {
            case 1045 ->
                    System.out.println("Error: usuario o contraseña de base de datos incorrectos.");
            case 1044, 1142, 1143 ->
                    System.out.println("Permiso denegado: tu usuario no tiene autorización para realizar esta operación.");
            case 1049 ->
                    System.out.println("Error: la base de datos no existe.");
            case CategoriaDAO.ERR_DUPLICADO ->
                    System.out.println("Error: ya existe un registro con ese valor único.");
            case CategoriaDAO.ERR_EN_USO ->
                    System.out.println("Error: no se puede eliminar, existen dependencias asociadas a este registro.");
            default -> {
                String estado = e.getSQLState();
                if (e.getMessage() != null && e.getMessage().contains("No suitable driver")) {
                    System.out.println("Error: falta el driver de MariaDB en el proyecto.");
                } else if (estado != null && estado.startsWith("08")) {
                    System.out.println("Error: no se pudo conectar. ¿Está iniciado MySQL en XAMPP?");
                } else {
                    System.out.println("Error de base de datos (" + e.getErrorCode() + "): " + e.getMessage());
                }
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número decimal válido.");
            }
        }
    }
}
