package io.snake.com.ui;

import io.snake.com.config.ConexionDB;
import io.snake.com.dao.*;
import io.snake.com.dto.*;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CafeteriaFX extends Application {

    private final CategoriaDAO categoriaDao = new CategoriaDAO();
    private final ProductoDAO productoDao = new ProductoDAO();
    private final CiudadDAO ciudadDao = new CiudadDAO();
    private final ClienteDAO clienteDao = new ClienteDAO();
    private final VendedorDAO vendedorDao = new VendedorDAO();
    private final VentaDAO ventaDao = new VentaDAO();
    private final DetalleVentaDAO detalleVentaDao = new DetalleVentaDAO();

    private Stage primaryStage;
    private Label lblEstadoGlobal;

    // Listas observables para las 7 tablas
    private final ObservableList<CategoriaDTO> listaCategorias = FXCollections.observableArrayList();
    private final ObservableList<ProductoDTO> listaProductos = FXCollections.observableArrayList();
    private final ObservableList<CiudadDTO> listaCiudades = FXCollections.observableArrayList();
    private final ObservableList<ClienteDTO> listaClientes = FXCollections.observableArrayList();
    private final ObservableList<VendedorDTO> listaVendedores = FXCollections.observableArrayList();
    private final ObservableList<VentaDTO> listaVentas = FXCollections.observableArrayList();
    private final ObservableList<DetalleVentaDTO> listaDetalles = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Sistema de Cafetería - SNAKE");

        this.primaryStage.setOnCloseRequest(e -> ConexionDB.cerrarSesion());

        if (ConexionDB.getUsuario() == null) {
            mostrarVistaLogin();
        } else {
            mostrarVistaPrincipal();
        }

        this.primaryStage.show();
    }

    // ==================== VISTA DE INICIO DE SESIÓN ====================

    public void mostrarVistaLogin() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2C1A12, #4E2F21);");

        VBox card = new VBox(16);
        card.setMaxWidth(420);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30));
        card.setStyle(
                "-fx-background-color: #FAF6F2;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.35), 16, 0, 0, 6);"
        );

        Label lblTitulo = new Label("INICIO DE SESIÓN - CAFETERÍA");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 19));
        lblTitulo.setTextFill(Color.web("#2C1A12"));

        Label lblSubtitulo = new Label("Conéctate con tu usuario de Base de Datos MariaDB");
        lblSubtitulo.setFont(Font.font("System", 13));
        lblSubtitulo.setTextFill(Color.web("#6D5446"));

        Label lblSugeridos = new Label("Usuarios disponibles (clic para autocompletar):");
        lblSugeridos.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        lblSugeridos.setTextFill(Color.web("#4E2F21"));

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ej. user_lectura, user_estructura, user_completo");
        txtUsuario.setStyle("-fx-font-size: 13px; -fx-padding: 8;");

        HBox boxChips = new HBox(8);
        boxChips.setAlignment(Pos.CENTER_LEFT);
        String[] usuariosDisponibles = {"user_lectura", "user_estructura", "user_completo"};
        for (String u : usuariosDisponibles) {
            Button btnChip = new Button(u);
            btnChip.setStyle(
                    "-fx-background-color: #E8DDD3; -fx-text-fill: #2C1A12; " +
                    "-fx-background-radius: 15; -fx-font-size: 11px; -fx-cursor: hand;"
            );
            btnChip.setOnAction(e -> txtUsuario.setText(u));
            boxChips.getChildren().add(btnChip);
        }

        Label lblUsuario = new Label("Usuario de BD:");
        lblUsuario.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblUsuario.setTextFill(Color.web("#2C1A12"));

        Label lblPassword = new Label("Contraseña:");
        lblPassword.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblPassword.setTextFill(Color.web("#2C1A12"));

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Ingresa la contraseña de BD");
        txtPassword.setStyle("-fx-font-size: 13px; -fx-padding: 8;");

        Label lblMensajeError = new Label();
        lblMensajeError.setWrapText(true);
        lblMensajeError.setTextFill(Color.web("#B71C1C"));
        lblMensajeError.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        lblMensajeError.setVisible(false);
        lblMensajeError.setManaged(false);

        Button btnIngresar = new Button("Iniciar Sesión");
        btnIngresar.setDefaultButton(true);
        btnIngresar.setMaxWidth(Double.MAX_VALUE);
        btnIngresar.setStyle(
                "-fx-background-color: #6F4E37; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10; " +
                "-fx-background-radius: 8; -fx-cursor: hand;"
        );

        btnIngresar.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();

            if (usuario.isEmpty()) {
                lblMensajeError.setText("Por favor ingresa un usuario de base de datos.");
                lblMensajeError.setVisible(true);
                lblMensajeError.setManaged(true);
                return;
            }

            try {
                ConexionDB.iniciarSesion(usuario, password);
                mostrarVistaPrincipal();
            } catch (SQLException ex) {
                String msg;
                if (ex.getErrorCode() == 1045) {
                    msg = "Error: Usuario o contraseña incorrectos. Inténtalo de nuevo.";
                } else {
                    msg = obtenerMensajeErrorSQL(ex);
                }
                lblMensajeError.setText(msg);
                lblMensajeError.setVisible(true);
                lblMensajeError.setManaged(true);
            }
        });

        card.getChildren().addAll(
                lblTitulo,
                lblSubtitulo,
                new Separator(),
                lblSugeridos,
                boxChips,
                lblUsuario,
                txtUsuario,
                lblPassword,
                txtPassword,
                lblMensajeError,
                btnIngresar
        );

        root.getChildren().add(card);

        Scene scene = new Scene(root, 560, 500);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    // ==================== VISTA PRINCIPAL (MENÚ CRUD) ====================

    public void mostrarVistaPrincipal() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FAF6F2;");

        // Barra superior (Header)
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 22, 14, 22));
        header.setStyle("-fx-background-color: #2C1A12;");

        Label lblApp = new Label("MENÚ PRINCIPAL - CAFETERÍA");
        lblApp.setFont(Font.font("System", FontWeight.BOLD, 17));
        lblApp.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRecargarTodo = new Button("Recargar Tablas");
        btnRecargarTodo.setStyle(
                "-fx-background-color: #6F4E37; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 12; -fx-cursor: hand;"
        );
        btnRecargarTodo.setOnAction(e -> recargarTodasLasTablas());

        Label lblUsuarioActivo = new Label("Usuario BD: " + ConexionDB.getUsuario());
        lblUsuarioActivo.setStyle(
                "-fx-background-color: #4E2F21; -fx-text-fill: #F5E6D3; " +
                "-fx-padding: 6 12; -fx-background-radius: 15; -fx-font-weight: bold;"
        );

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setStyle(
                "-fx-background-color: #C62828; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 6 14; -fx-cursor: hand;"
        );
        btnCerrarSesion.setOnAction(e -> {
            ConexionDB.cerrarSesion();
            listaCategorias.clear();
            listaProductos.clear();
            listaCiudades.clear();
            listaClientes.clear();
            listaVendedores.clear();
            listaVentas.clear();
            listaDetalles.clear();
            mostrarVistaLogin();
        });

        header.getChildren().addAll(lblApp, spacer, btnRecargarTodo, lblUsuarioActivo, btnCerrarSesion);
        root.setTop(header);

        // Barra de estado inferior
        HBox statusBar = new HBox(10);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(8, 18, 8, 18));
        statusBar.setStyle("-fx-background-color: #E8DDD3; -fx-border-color: #D3C2B2; -fx-border-width: 1 0 0 0;");

        lblEstadoGlobal = new Label("Sesión iniciada correctamente como [" + ConexionDB.getUsuario() + "].");
        lblEstadoGlobal.setFont(Font.font("System", 12));
        lblEstadoGlobal.setTextFill(Color.web("#2C1A12"));
        statusBar.getChildren().add(lblEstadoGlobal);
        root.setBottom(statusBar);

        // Pestañas de los 7 módulos de la BD cafeteria
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 13px;");

        Tab tabCategorias = new Tab("Categorías", crearPanelCategorias());
        Tab tabProductos = new Tab("Productos", crearPanelProductos());
        Tab tabCiudades = new Tab("Ciudades", crearPanelCiudades());
        Tab tabClientes = new Tab("Clientes", crearPanelClientes());
        Tab tabVendedores = new Tab("Vendedores", crearPanelVendedores());
        Tab tabVentas = new Tab("Ventas", crearPanelVentas());
        Tab tabDetalles = new Tab("Detalle de Venta", crearPanelDetalleVenta());

        tabPane.getTabs().addAll(
                tabCategorias,
                tabProductos,
                tabCiudades,
                tabClientes,
                tabVendedores,
                tabVentas,
                tabDetalles
        );
        root.setCenter(tabPane);

        // Carga inicial silenciosa de todas las tablas
        recargarTodasLasTablas();

        Scene scene = new Scene(root, 1080, 670);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void recargarTodasLasTablas() {
        cargarCategorias(false);
        cargarProductos(false);
        cargarCiudades(false);
        cargarClientes(false);
        cargarVendedores(false);
        cargarVentas(false);
        cargarDetallesVenta(false);
        actualizarEstado("Datos sincronizados con la base de datos como [" + ConexionDB.getUsuario() + "].");
    }

    // ==================== 1. PANEL DE CATEGORÍAS ====================

    private Pane crearPanelCategorias() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Categoría por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("ID a buscar");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Categorías", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Categoría");

        TextField txtId = new TextField();
        txtId.setPromptText("ID (Automático al crear)");
        txtId.setEditable(false);
        txtId.setStyle("-fx-background-color: #EFEBE9;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la categoría");

        Button btnCrear = estilizarBoton("Crear Categoría", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Categoría", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Categoría", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Categoría:"), txtId,
                new Label("Nombre:"), txtNombre,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<CategoriaDTO> tabla = new TableView<>(listaCategorias);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay categorías registradas o listadas."));

        TableColumn<CategoriaDTO, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colId.setMaxWidth(110);
        colId.setMinWidth(70);

        TableColumn<CategoriaDTO, String> colNombre = new TableColumn<>("Nombre de Categoría");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        tabla.getColumns().addAll(List.of(colId, colNombre));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtId.setText(String.valueOf(newSel.getId()));
                txtNombre.setText(newSel.getNombre());
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtId.clear();
            txtNombre.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de categorías limpio.");
        });

        btnListar.setOnAction(e -> cargarCategorias(true));

        btnBuscar.setOnAction(e -> {
            Integer id = validarEntero(txtBuscarId.getText(), "ID de categoría a buscar");
            if (id == null) return;
            try {
                CategoriaDTO c = categoriaDao.buscarPorId(id);
                if (c != null) {
                    listaCategorias.setAll(c);
                    tabla.getSelectionModel().selectFirst();
                    txtId.setText(String.valueOf(c.getId()));
                    txtNombre.setText(c.getNombre());
                    actualizarEstado("Categoría encontrada: " + c);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Categoría",
                            "No existe una categoría con ID " + id);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre no puede estar vacío.");
                return;
            }
            try {
                CategoriaDTO creada = categoriaDao.crear(new CategoriaDTO(nombre));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Categoría Creada",
                        "Categoría creada con ID " + creada.getId());
                txtId.setText(String.valueOf(creada.getId()));
                cargarCategorias(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtId.getText(), txtBuscarId.getText(), "Selecciona o busca una categoría para actualizar.");
            if (id == null) return;

            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre no puede estar vacío.");
                return;
            }
            try {
                CategoriaDTO existente = categoriaDao.buscarPorId(id);
                if (existente == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una categoría con ID " + id);
                    return;
                }
                existente.setNombre(nuevoNombre);
                boolean ok = categoriaDao.actualizar(existente);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Categoría",
                        ok ? "Categoría actualizada." : "No se realizaron cambios.");
                cargarCategorias(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtId.getText(), txtBuscarId.getText(), "Selecciona o busca una categoría para eliminar.");
            if (id == null) return;

            try {
                CategoriaDTO c = categoriaDao.buscarPorId(id);
                if (c == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una categoría con ID " + id);
                    return;
                }
                if (confirmarAccion("Eliminar Categoría", "¿Eliminar \"" + c.getNombre() + "\" (ID: " + id + ")?")) {
                    boolean eliminado = categoriaDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Categoría",
                            eliminado ? "Categoría eliminada." : "No se pudo eliminar.");
                    txtId.clear();
                    txtNombre.clear();
                    cargarCategorias(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 2. PANEL DE PRODUCTOS ====================

    private Pane crearPanelProductos() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Producto por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("ID de producto");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Productos", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Producto");

        TextField txtIdProducto = new TextField();
        txtIdProducto.setPromptText("ID (Automático al crear)");
        txtIdProducto.setEditable(false);
        txtIdProducto.setStyle("-fx-background-color: #EFEBE9;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");

        TextField txtIdCategoria = new TextField();
        txtIdCategoria.setPromptText("ID de categoría (ej. 1)");

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio de referencia (ej. 45.00)");

        Button btnCrear = estilizarBoton("Crear Producto", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Producto", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Producto", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Producto:"), txtIdProducto,
                new Label("Nombre del Producto:"), txtNombre,
                new Label("ID de Categoría:"), txtIdCategoria,
                new Label("Precio de Referencia ($):"), txtPrecio,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<ProductoDTO> tabla = new TableView<>(listaProductos);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay productos registrados o listados."));

        TableColumn<ProductoDTO, Integer> colId = new TableColumn<>("ID Producto");
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getIdProducto()).asObject());
        colId.setMaxWidth(110);

        TableColumn<ProductoDTO, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNombre()));

        TableColumn<ProductoDTO, Integer> colCat = new TableColumn<>("ID Categoría");
        colCat.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getIdCategoria()).asObject());
        colCat.setMaxWidth(120);

        TableColumn<ProductoDTO, String> colPrecio = new TableColumn<>("Precio Referencia");
        colPrecio.setCellValueFactory(p -> new SimpleStringProperty(String.format("$%.2f", p.getValue().getPrecioReferencia())));

        tabla.getColumns().addAll(List.of(colId, colNombre, colCat, colPrecio));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdProducto.setText(String.valueOf(newSel.getIdProducto()));
                txtNombre.setText(newSel.getNombre());
                txtIdCategoria.setText(String.valueOf(newSel.getIdCategoria()));
                txtPrecio.setText(String.valueOf(newSel.getPrecioReferencia()));
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdProducto.clear();
            txtNombre.clear();
            txtIdCategoria.clear();
            txtPrecio.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de productos limpio.");
        });

        btnListar.setOnAction(e -> cargarProductos(true));

        btnBuscar.setOnAction(e -> {
            Integer id = validarEntero(txtBuscarId.getText(), "ID del producto a buscar");
            if (id == null) return;
            try {
                ProductoDTO p = productoDao.buscarPorId(id);
                if (p != null) {
                    listaProductos.setAll(p);
                    tabla.getSelectionModel().selectFirst();
                    txtIdProducto.setText(String.valueOf(p.getIdProducto()));
                    txtNombre.setText(p.getNombre());
                    txtIdCategoria.setText(String.valueOf(p.getIdCategoria()));
                    txtPrecio.setText(String.valueOf(p.getPrecioReferencia()));
                    actualizarEstado("Producto encontrado: " + p);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Producto",
                            "No existe un producto con el ID " + id);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre no puede estar vacío.");
                return;
            }
            Integer idCategoria = validarEntero(txtIdCategoria.getText(), "ID de la categoría");
            if (idCategoria == null) return;

            Double precio = validarDecimal(txtPrecio.getText(), "Precio de referencia");
            if (precio == null) return;

            try {
                ProductoDTO nuevo = new ProductoDTO(nombre, idCategoria, precio);
                ProductoDTO creado = productoDao.crear(nuevo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto Creado",
                        "¡Producto creado con éxito con el ID: " + creado.getIdProducto() + "!");
                txtIdProducto.setText(String.valueOf(creado.getIdProducto()));
                cargarProductos(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdProducto.getText(), txtBuscarId.getText(), "Selecciona o busca un producto para actualizar.");
            if (id == null) return;

            String nuevoNombre = txtNombre.getText().trim();
            Integer nuevaCat = validarEntero(txtIdCategoria.getText(), "Nuevo ID de categoría");
            if (nuevaCat == null) return;

            Double nuevoPrecio = validarDecimal(txtPrecio.getText(), "Nuevo precio");
            if (nuevoPrecio == null) return;

            try {
                ProductoDTO p = productoDao.buscarPorId(id);
                if (p == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un producto con ese ID.");
                    return;
                }
                if (!nuevoNombre.isEmpty()) {
                    p.setNombre(nuevoNombre);
                }
                p.setIdCategoria(nuevaCat);
                p.setPrecioReferencia(nuevoPrecio);

                boolean actualizado = productoDao.actualizar(p);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Producto",
                        actualizado ? "Producto actualizado correctamente." : "No se realizaron cambios.");
                cargarProductos(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdProducto.getText(), txtBuscarId.getText(), "Selecciona o busca un producto para eliminar.");
            if (id == null) return;

            try {
                ProductoDTO p = productoDao.buscarPorId(id);
                if (p == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un producto con ese ID.");
                    return;
                }
                if (confirmarAccion("Eliminar Producto", "¿Estás seguro de eliminar el producto \"" + p.getNombre() + "\"?")) {
                    boolean eliminado = productoDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Producto",
                            eliminado ? "Producto eliminado con éxito." : "No se pudo eliminar.");
                    txtIdProducto.clear();
                    txtNombre.clear();
                    txtIdCategoria.clear();
                    txtPrecio.clear();
                    cargarProductos(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 3. PANEL DE CIUDADES ====================

    private Pane crearPanelCiudades() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Ciudad por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("ID de ciudad");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Ciudades", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Ciudad");

        TextField txtIdCiudad = new TextField();
        txtIdCiudad.setPromptText("ID (Automático al crear)");
        txtIdCiudad.setEditable(false);
        txtIdCiudad.setStyle("-fx-background-color: #EFEBE9;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la ciudad (ej. CDMX)");

        Button btnCrear = estilizarBoton("Crear Ciudad", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Ciudad", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Ciudad", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Ciudad:"), txtIdCiudad,
                new Label("Nombre de la Ciudad:"), txtNombre,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<CiudadDTO> tabla = new TableView<>(listaCiudades);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay ciudades registradas o listadas."));

        TableColumn<CiudadDTO, Integer> colId = new TableColumn<>("ID Ciudad");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCiudad()).asObject());
        colId.setMaxWidth(110);
        colId.setMinWidth(70);

        TableColumn<CiudadDTO, String> colNombre = new TableColumn<>("Nombre de Ciudad");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        tabla.getColumns().addAll(List.of(colId, colNombre));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdCiudad.setText(String.valueOf(newSel.getIdCiudad()));
                txtNombre.setText(newSel.getNombre());
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdCiudad.clear();
            txtNombre.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de ciudades limpio.");
        });

        btnListar.setOnAction(e -> cargarCiudades(true));

        btnBuscar.setOnAction(e -> {
            Integer id = validarEntero(txtBuscarId.getText(), "ID de ciudad a buscar");
            if (id == null) return;
            try {
                CiudadDTO c = ciudadDao.buscarPorId(id);
                if (c != null) {
                    listaCiudades.setAll(c);
                    tabla.getSelectionModel().selectFirst();
                    txtIdCiudad.setText(String.valueOf(c.getIdCiudad()));
                    txtNombre.setText(c.getNombre());
                    actualizarEstado("Ciudad encontrada: " + c);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Ciudad",
                            "No existe una ciudad con ID " + id);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre de la ciudad no puede estar vacío.");
                return;
            }
            try {
                CiudadDTO creada = ciudadDao.crear(new CiudadDTO(nombre));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Ciudad Creada",
                        "Ciudad creada con ID " + creada.getIdCiudad());
                txtIdCiudad.setText(String.valueOf(creada.getIdCiudad()));
                cargarCiudades(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdCiudad.getText(), txtBuscarId.getText(), "Selecciona o busca una ciudad para actualizar.");
            if (id == null) return;

            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre de la ciudad no puede estar vacío.");
                return;
            }
            try {
                CiudadDTO existente = ciudadDao.buscarPorId(id);
                if (existente == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una ciudad con ID " + id);
                    return;
                }
                existente.setNombre(nuevoNombre);
                boolean ok = ciudadDao.actualizar(existente);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Ciudad",
                        ok ? "Ciudad actualizada correctamente." : "No se realizaron cambios.");
                cargarCiudades(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdCiudad.getText(), txtBuscarId.getText(), "Selecciona o busca una ciudad para eliminar.");
            if (id == null) return;

            try {
                CiudadDTO c = ciudadDao.buscarPorId(id);
                if (c == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una ciudad con ID " + id);
                    return;
                }
                if (confirmarAccion("Eliminar Ciudad", "¿Eliminar la ciudad \"" + c.getNombre() + "\" (ID: " + id + ")?")) {
                    boolean eliminado = ciudadDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Ciudad",
                            eliminado ? "Ciudad eliminada con éxito." : "No se pudo eliminar.");
                    txtIdCiudad.clear();
                    txtNombre.clear();
                    cargarCiudades(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 4. PANEL DE CLIENTES ====================

    private Pane crearPanelClientes() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Cliente por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("ID de cliente");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Clientes", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Cliente");

        TextField txtIdCliente = new TextField();
        txtIdCliente.setPromptText("ID (Automático al crear)");
        txtIdCliente.setEditable(false);
        txtIdCliente.setStyle("-fx-background-color: #EFEBE9;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("correo@ejemplo.com");

        TextField txtIdCiudad = new TextField();
        txtIdCiudad.setPromptText("ID de ciudad (ej. 1)");

        Button btnCrear = estilizarBoton("Crear Cliente", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Cliente", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Cliente", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Cliente:"), txtIdCliente,
                new Label("Nombre:"), txtNombre,
                new Label("Email:"), txtEmail,
                new Label("ID de Ciudad:"), txtIdCiudad,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<ClienteDTO> tabla = new TableView<>(listaClientes);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay clientes registrados o listados."));

        TableColumn<ClienteDTO, Integer> colId = new TableColumn<>("ID Cliente");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCliente()).asObject());
        colId.setMaxWidth(100);

        TableColumn<ClienteDTO, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        TableColumn<ClienteDTO, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<ClienteDTO, Integer> colCiudad = new TableColumn<>("ID Ciudad");
        colCiudad.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdCiudad()).asObject());
        colCiudad.setMaxWidth(110);

        tabla.getColumns().addAll(List.of(colId, colNombre, colEmail, colCiudad));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdCliente.setText(String.valueOf(newSel.getIdCliente()));
                txtNombre.setText(newSel.getNombre());
                txtEmail.setText(newSel.getEmail());
                txtIdCiudad.setText(String.valueOf(newSel.getIdCiudad()));
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdCliente.clear();
            txtNombre.clear();
            txtEmail.clear();
            txtIdCiudad.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de clientes limpio.");
        });

        btnListar.setOnAction(e -> cargarClientes(true));

        btnBuscar.setOnAction(e -> {
            Integer id = validarEntero(txtBuscarId.getText(), "ID del cliente a buscar");
            if (id == null) return;
            try {
                ClienteDTO c = clienteDao.buscarPorId(id);
                if (c != null) {
                    listaClientes.setAll(c);
                    tabla.getSelectionModel().selectFirst();
                    txtIdCliente.setText(String.valueOf(c.getIdCliente()));
                    txtNombre.setText(c.getNombre());
                    txtEmail.setText(c.getEmail());
                    txtIdCiudad.setText(String.valueOf(c.getIdCiudad()));
                    actualizarEstado("Cliente encontrado: " + c);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Cliente",
                            "No existe un cliente con el ID " + id);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            if (nombre.isEmpty() || email.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre y el email son obligatorios.");
                return;
            }
            Integer idCiudad = validarEntero(txtIdCiudad.getText(), "ID de la ciudad");
            if (idCiudad == null) return;

            try {
                ClienteDTO nuevo = new ClienteDTO(nombre, email, idCiudad);
                ClienteDTO creado = clienteDao.crear(nuevo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Cliente Creado",
                        "¡Cliente creado con éxito con el ID: " + creado.getIdCliente() + "!");
                txtIdCliente.setText(String.valueOf(creado.getIdCliente()));
                cargarClientes(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdCliente.getText(), txtBuscarId.getText(), "Selecciona o busca un cliente para actualizar.");
            if (id == null) return;

            String nuevoNombre = txtNombre.getText().trim();
            String nuevoEmail = txtEmail.getText().trim();
            if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre y el email no pueden estar vacíos.");
                return;
            }
            Integer nuevaCiudad = validarEntero(txtIdCiudad.getText(), "ID de ciudad");
            if (nuevaCiudad == null) return;

            try {
                ClienteDTO c = clienteDao.buscarPorId(id);
                if (c == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un cliente con ese ID.");
                    return;
                }
                c.setNombre(nuevoNombre);
                c.setEmail(nuevoEmail);
                c.setIdCiudad(nuevaCiudad);

                boolean actualizado = clienteDao.actualizar(c);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Cliente",
                        actualizado ? "Cliente actualizado correctamente." : "No se realizaron cambios.");
                cargarClientes(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdCliente.getText(), txtBuscarId.getText(), "Selecciona o busca un cliente para eliminar.");
            if (id == null) return;

            try {
                ClienteDTO c = clienteDao.buscarPorId(id);
                if (c == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un cliente con ese ID.");
                    return;
                }
                if (confirmarAccion("Eliminar Cliente", "¿Estás seguro de eliminar al cliente \"" + c.getNombre() + "\" (ID: " + id + ")?")) {
                    boolean eliminado = clienteDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Cliente",
                            eliminado ? "Cliente eliminado con éxito." : "No se pudo eliminar.");
                    txtIdCliente.clear();
                    txtNombre.clear();
                    txtEmail.clear();
                    txtIdCiudad.clear();
                    cargarClientes(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 5. PANEL DE VENDEDORES ====================

    private Pane crearPanelVendedores() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Vendedor por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("Ej. V-01");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Vendedores", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Vendedor");

        TextField txtIdVendedor = new TextField();
        txtIdVendedor.setPromptText("Código ID (ej. V-04)");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo del vendedor");

        Button btnCrear = estilizarBoton("Crear Vendedor", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Vendedor", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Vendedor", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Vendedor (máx. 10 car.):"), txtIdVendedor,
                new Label("Nombre del Vendedor:"), txtNombre,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<VendedorDTO> tabla = new TableView<>(listaVendedores);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay vendedores registrados o listados."));

        TableColumn<VendedorDTO, String> colId = new TableColumn<>("ID Vendedor");
        colId.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getIdVendedor()));
        colId.setMaxWidth(140);
        colId.setMinWidth(90);

        TableColumn<VendedorDTO, String> colNombre = new TableColumn<>("Nombre del Vendedor");
        colNombre.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getNombre()));

        tabla.getColumns().addAll(List.of(colId, colNombre));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdVendedor.setText(newSel.getIdVendedor());
                txtNombre.setText(newSel.getNombre());
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdVendedor.clear();
            txtNombre.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de vendedores limpio.");
        });

        btnListar.setOnAction(e -> cargarVendedores(true));

        btnBuscar.setOnAction(e -> {
            String id = txtBuscarId.getText().trim();
            if (id.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "ID Requerido", "Ingresa el ID del vendedor a buscar (ej. V-01).");
                return;
            }
            try {
                VendedorDTO v = vendedorDao.buscarPorId(id);
                if (v != null) {
                    listaVendedores.setAll(v);
                    tabla.getSelectionModel().selectFirst();
                    txtIdVendedor.setText(v.getIdVendedor());
                    txtNombre.setText(v.getNombre());
                    actualizarEstado("Vendedor encontrado: " + v);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Vendedor",
                            "No existe un vendedor con ID \"" + id + "\"");
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            String id = txtIdVendedor.getText().trim();
            String nombre = txtNombre.getText().trim();
            if (id.isEmpty() || nombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El ID del vendedor y el nombre son obligatorios.");
                return;
            }
            try {
                VendedorDTO creado = vendedorDao.crear(new VendedorDTO(id, nombre));
                mostrarAlerta(Alert.AlertType.INFORMATION, "Vendedor Creado",
                        "¡Vendedor creado con ID: " + creado.getIdVendedor() + "!");
                cargarVendedores(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            String id = !txtIdVendedor.getText().trim().isEmpty() ? txtIdVendedor.getText().trim() : txtBuscarId.getText().trim();
            if (id.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "ID Requerido", "Selecciona o ingresa el ID del vendedor a actualizar.");
                return;
            }
            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El nombre del vendedor no puede estar vacío.");
                return;
            }
            try {
                VendedorDTO existente = vendedorDao.buscarPorId(id);
                if (existente == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un vendedor con ID \"" + id + "\".");
                    return;
                }
                existente.setNombre(nuevoNombre);
                boolean ok = vendedorDao.actualizar(existente);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Vendedor",
                        ok ? "Vendedor actualizado correctamente." : "No se realizaron cambios.");
                cargarVendedores(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            String id = !txtIdVendedor.getText().trim().isEmpty() ? txtIdVendedor.getText().trim() : txtBuscarId.getText().trim();
            if (id.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "ID Requerido", "Selecciona o ingresa el ID del vendedor a eliminar.");
                return;
            }
            try {
                VendedorDTO v = vendedorDao.buscarPorId(id);
                if (v == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe un vendedor con ID \"" + id + "\".");
                    return;
                }
                if (confirmarAccion("Eliminar Vendedor", "¿Eliminar al vendedor \"" + v.getNombre() + "\" (ID: " + id + ")?")) {
                    boolean eliminado = vendedorDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Vendedor",
                            eliminado ? "Vendedor eliminado con éxito." : "No se pudo eliminar.");
                    txtIdVendedor.clear();
                    txtNombre.clear();
                    cargarVendedores(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 6. PANEL DE VENTAS ====================

    private Pane crearPanelVentas() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscar = new Label("Buscar Venta por ID:");
        lblBuscar.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarId = new TextField();
        txtBuscarId.setPromptText("ID de venta");
        txtBuscarId.setPrefWidth(130);

        Button btnBuscar = estilizarBoton("Buscar por ID", "#5D4037");
        Button btnListar = estilizarBoton("Listar Ventas", "#6F4E37");

        barraBusqueda.getChildren().addAll(lblBuscar, txtBuscarId, btnBuscar, btnListar);
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Venta");

        TextField txtIdVenta = new TextField();
        txtIdVenta.setPromptText("ID (Automático al registrar)");
        txtIdVenta.setEditable(false);
        txtIdVenta.setStyle("-fx-background-color: #EFEBE9;");

        DatePicker dpFecha = new DatePicker(LocalDate.now());
        dpFecha.setMaxWidth(Double.MAX_VALUE);

        TextField txtIdCliente = new TextField();
        txtIdCliente.setPromptText("Ej. 1");

        TextField txtIdVendedor = new TextField();
        txtIdVendedor.setPromptText("Ej. V-01");

        TextField txtTotal = new TextField();
        txtTotal.setPromptText("Ej. 125.50");

        Button btnRegistrar = estilizarBoton("Registrar Venta", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Venta", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Venta", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnRegistrar.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Venta:"), txtIdVenta,
                new Label("Fecha de Venta:"), dpFecha,
                new Label("ID del Cliente:"), txtIdCliente,
                new Label("ID del Vendedor:"), txtIdVendedor,
                new Label("Total de la Venta ($):"), txtTotal,
                new Separator(),
                btnRegistrar, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<VentaDTO> tabla = new TableView<>(listaVentas);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay ventas registradas o listadas."));

        TableColumn<VentaDTO, Integer> colId = new TableColumn<>("ID Venta");
        colId.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getIdVenta()).asObject());
        colId.setMaxWidth(100);

        TableColumn<VentaDTO, LocalDate> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getFechaVenta()));

        TableColumn<VentaDTO, Integer> colCliente = new TableColumn<>("ID Cliente");
        colCliente.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getIdCliente()).asObject());

        TableColumn<VentaDTO, String> colVendedor = new TableColumn<>("ID Vendedor");
        colVendedor.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getIdVendedor()));

        TableColumn<VentaDTO, String> colTotal = new TableColumn<>("Total ($)");
        colTotal.setCellValueFactory(v -> new SimpleStringProperty(String.format("$%.2f", v.getValue().getTotalVenta())));

        tabla.getColumns().addAll(List.of(colId, colFecha, colCliente, colVendedor, colTotal));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdVenta.setText(String.valueOf(newSel.getIdVenta()));
                dpFecha.setValue(newSel.getFechaVenta());
                txtIdCliente.setText(String.valueOf(newSel.getIdCliente()));
                txtIdVendedor.setText(newSel.getIdVendedor());
                txtTotal.setText(String.valueOf(newSel.getTotalVenta()));
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdVenta.clear();
            dpFecha.setValue(LocalDate.now());
            txtIdCliente.clear();
            txtIdVendedor.clear();
            txtTotal.clear();
            txtBuscarId.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de ventas limpio.");
        });

        btnListar.setOnAction(e -> cargarVentas(true));

        btnBuscar.setOnAction(e -> {
            Integer id = validarEntero(txtBuscarId.getText(), "ID de la venta a buscar");
            if (id == null) return;
            try {
                VentaDTO v = ventaDao.buscarPorId(id);
                if (v != null) {
                    listaVentas.setAll(v);
                    tabla.getSelectionModel().selectFirst();
                    txtIdVenta.setText(String.valueOf(v.getIdVenta()));
                    dpFecha.setValue(v.getFechaVenta());
                    txtIdCliente.setText(String.valueOf(v.getIdCliente()));
                    txtIdVendedor.setText(v.getIdVendedor());
                    txtTotal.setText(String.valueOf(v.getTotalVenta()));
                    actualizarEstado("Venta encontrada: " + v);
                } else {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Venta",
                            "No existe una venta con el ID " + id);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnRegistrar.setOnAction(e -> {
            LocalDate fecha = dpFecha.getValue() != null ? dpFecha.getValue() : LocalDate.now();
            Integer idCliente = validarEntero(txtIdCliente.getText(), "ID del cliente");
            if (idCliente == null) return;

            String idVendedor = txtIdVendedor.getText().trim();
            if (idVendedor.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingresa el ID del vendedor (ej. V-01).");
                return;
            }

            Double total = validarDecimal(txtTotal.getText(), "Total de la venta");
            if (total == null) return;

            try {
                VentaDTO nuevaVenta = new VentaDTO(fecha, idCliente, idVendedor, total);
                VentaDTO creada = ventaDao.crear(nuevaVenta);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Venta Registrada",
                        "¡Venta registrada exitosamente con el ID: " + creada.getIdVenta() + "!");
                txtIdVenta.setText(String.valueOf(creada.getIdVenta()));
                cargarVentas(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdVenta.getText(), txtBuscarId.getText(), "Selecciona o busca una venta para actualizar.");
            if (id == null) return;

            Integer nuevoCliente = validarEntero(txtIdCliente.getText(), "Nuevo ID de cliente");
            if (nuevoCliente == null) return;

            String nuevoVendedor = txtIdVendedor.getText().trim();
            if (nuevoVendedor.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El ID del vendedor no puede estar vacío.");
                return;
            }

            Double nuevoTotal = validarDecimal(txtTotal.getText(), "Nuevo total");
            if (nuevoTotal == null) return;

            try {
                VentaDTO v = ventaDao.buscarPorId(id);
                if (v == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una venta con ese ID.");
                    return;
                }
                if (dpFecha.getValue() != null) {
                    v.setFechaVenta(dpFecha.getValue());
                }
                v.setIdCliente(nuevoCliente);
                v.setIdVendedor(nuevoVendedor);
                v.setTotalVenta(nuevoTotal);

                boolean actualizado = ventaDao.actualizar(v);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Venta",
                        actualizado ? "Venta actualizada correctamente." : "No se pudo actualizar la venta.");
                cargarVentas(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer id = obtenerIdSeleccionadoOBuscado(txtIdVenta.getText(), txtBuscarId.getText(), "Selecciona o busca una venta para eliminar.");
            if (id == null) return;

            try {
                VentaDTO v = ventaDao.buscarPorId(id);
                if (v == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrada", "No existe una venta con ese ID.");
                    return;
                }
                if (confirmarAccion("Eliminar Venta", "¿Estás seguro de eliminar la venta " + id + "?")) {
                    boolean eliminado = ventaDao.eliminar(id);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Venta",
                            eliminado ? "Venta eliminada con éxito." : "No se pudo eliminar.");
                    txtIdVenta.clear();
                    txtIdCliente.clear();
                    txtIdVendedor.clear();
                    txtTotal.clear();
                    cargarVentas(false);
                    cargarDetallesVenta(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== 7. PANEL DE DETALLE DE VENTA ====================

    private Pane crearPanelDetalleVenta() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(18));

        HBox barraBusqueda = new HBox(10);
        barraBusqueda.setAlignment(Pos.CENTER_LEFT);
        barraBusqueda.setPadding(new Insets(0, 0, 14, 0));

        Label lblBuscarVenta = new Label("ID Venta:");
        lblBuscarVenta.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarVenta = new TextField();
        txtBuscarVenta.setPromptText("Ej. 1001");
        txtBuscarVenta.setPrefWidth(110);

        Label lblBuscarProd = new Label("ID Producto (opc.):");
        lblBuscarProd.setFont(Font.font("System", FontWeight.BOLD, 13));
        TextField txtBuscarProd = new TextField();
        txtBuscarProd.setPromptText("Ej. 3");
        txtBuscarProd.setPrefWidth(110);

        Button btnBuscar = estilizarBoton("Buscar Detalle(s)", "#5D4037");
        Button btnListar = estilizarBoton("Listar Todos", "#6F4E37");

        barraBusqueda.getChildren().addAll(
                lblBuscarVenta, txtBuscarVenta,
                lblBuscarProd, txtBuscarProd,
                btnBuscar, btnListar
        );
        panel.setTop(barraBusqueda);

        VBox form = crearTarjetaFormulario("Gestión de Detalle de Venta");

        TextField txtIdVenta = new TextField();
        txtIdVenta.setPromptText("ID de la venta (ej. 1001)");

        TextField txtIdProducto = new TextField();
        txtIdProducto.setPromptText("ID del producto (ej. 3)");

        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad (> 0)");

        Button btnCrear = estilizarBoton("Agregar Detalle", "#2E7D32");
        Button btnActualizar = estilizarBoton("Actualizar Cantidad", "#1565C0");
        Button btnEliminar = estilizarBoton("Eliminar Detalle", "#C62828");
        Button btnLimpiar = estilizarBoton("Limpiar Formulario", "#757575");

        btnCrear.setMaxWidth(Double.MAX_VALUE);
        btnActualizar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
                new Label("ID Venta:"), txtIdVenta,
                new Label("ID Producto:"), txtIdProducto,
                new Label("Cantidad:"), txtCantidad,
                new Separator(),
                btnCrear, btnActualizar, btnEliminar, btnLimpiar
        );

        TableView<DetalleVentaDTO> tabla = new TableView<>(listaDetalles);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabla.setPlaceholder(new Label("No hay detalles de venta registrados o listados."));

        TableColumn<DetalleVentaDTO, Integer> colVenta = new TableColumn<>("ID Venta");
        colVenta.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdVenta()).asObject());

        TableColumn<DetalleVentaDTO, Integer> colProducto = new TableColumn<>("ID Producto");
        colProducto.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdProducto()).asObject());

        TableColumn<DetalleVentaDTO, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCantidad()).asObject());

        tabla.getColumns().addAll(List.of(colVenta, colProducto, colCantidad));

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdVenta.setText(String.valueOf(newSel.getIdVenta()));
                txtIdProducto.setText(String.valueOf(newSel.getIdProducto()));
                txtCantidad.setText(String.valueOf(newSel.getCantidad()));
            }
        });

        btnLimpiar.setOnAction(e -> {
            txtIdVenta.clear();
            txtIdProducto.clear();
            txtCantidad.clear();
            txtBuscarVenta.clear();
            txtBuscarProd.clear();
            tabla.getSelectionModel().clearSelection();
            actualizarEstado("Formulario de detalle de venta limpio.");
        });

        btnListar.setOnAction(e -> cargarDetallesVenta(true));

        btnBuscar.setOnAction(e -> {
            Integer idVenta = validarEntero(txtBuscarVenta.getText(), "ID de Venta a buscar");
            if (idVenta == null) return;

            String prodTxt = txtBuscarProd.getText().trim();
            try {
                if (prodTxt.isEmpty()) {
                    List<DetalleVentaDTO> encontrados = detalleVentaDao.buscarPorVenta(idVenta);
                    if (!encontrados.isEmpty()) {
                        listaDetalles.setAll(encontrados);
                        tabla.getSelectionModel().selectFirst();
                        actualizarEstado("Detalles encontrados para la venta " + idVenta + ": " + encontrados.size());
                    } else {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Detalle",
                                "No existen detalles registrados para la venta " + idVenta);
                    }
                } else {
                    Integer idProd = validarEntero(prodTxt, "ID de Producto a buscar");
                    if (idProd == null) return;
                    DetalleVentaDTO d = detalleVentaDao.buscarPorId(idVenta, idProd);
                    if (d != null) {
                        listaDetalles.setAll(d);
                        tabla.getSelectionModel().selectFirst();
                        txtIdVenta.setText(String.valueOf(d.getIdVenta()));
                        txtIdProducto.setText(String.valueOf(d.getIdProducto()));
                        txtCantidad.setText(String.valueOf(d.getCantidad()));
                        actualizarEstado("Detalle encontrado: " + d);
                    } else {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Búsqueda de Detalle",
                                "No existe detalle para Venta " + idVenta + " y Producto " + idProd);
                    }
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnCrear.setOnAction(e -> {
            Integer idVenta = validarEntero(txtIdVenta.getText(), "ID de la venta");
            if (idVenta == null) return;

            Integer idProducto = validarEntero(txtIdProducto.getText(), "ID del producto");
            if (idProducto == null) return;

            Integer cantidad = validarEntero(txtCantidad.getText(), "Cantidad");
            if (cantidad == null) return;
            if (cantidad <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La cantidad debe ser mayor a 0.");
                return;
            }

            try {
                DetalleVentaDTO nuevo = new DetalleVentaDTO(idVenta, idProducto, cantidad);
                detalleVentaDao.crear(nuevo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Detalle Agregado",
                        "¡Detalle registrado (Venta: " + idVenta + ", Producto: " + idProducto + ", Cant: " + cantidad + ")!");
                cargarDetallesVenta(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnActualizar.setOnAction(e -> {
            Integer idVenta = validarEntero(txtIdVenta.getText(), "ID de la venta");
            if (idVenta == null) return;

            Integer idProducto = validarEntero(txtIdProducto.getText(), "ID del producto");
            if (idProducto == null) return;

            Integer nuevaCant = validarEntero(txtCantidad.getText(), "Nueva cantidad");
            if (nuevaCant == null) return;
            if (nuevaCant <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La cantidad debe ser mayor a 0.");
                return;
            }

            try {
                DetalleVentaDTO existente = detalleVentaDao.buscarPorId(idVenta, idProducto);
                if (existente == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                            "No existe un detalle con Venta ID " + idVenta + " y Producto ID " + idProducto);
                    return;
                }
                existente.setCantidad(nuevaCant);
                boolean ok = detalleVentaDao.actualizar(existente);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizar Detalle",
                        ok ? "Cantidad actualizada correctamente." : "No se realizaron cambios.");
                cargarDetallesVenta(false);
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        btnEliminar.setOnAction(e -> {
            Integer idVenta = validarEntero(txtIdVenta.getText(), "ID de la venta");
            if (idVenta == null) return;

            Integer idProducto = validarEntero(txtIdProducto.getText(), "ID del producto");
            if (idProducto == null) return;

            try {
                DetalleVentaDTO existente = detalleVentaDao.buscarPorId(idVenta, idProducto);
                if (existente == null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No encontrado",
                            "No existe un detalle con Venta ID " + idVenta + " y Producto ID " + idProducto);
                    return;
                }
                if (confirmarAccion("Eliminar Detalle de Venta",
                        "¿Eliminar el producto " + idProducto + " de la venta " + idVenta + "?")) {
                    boolean eliminado = detalleVentaDao.eliminar(idVenta, idProducto);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminar Detalle",
                            eliminado ? "Detalle de venta eliminado con éxito." : "No se pudo eliminar.");
                    txtIdVenta.clear();
                    txtIdProducto.clear();
                    txtCantidad.clear();
                    cargarDetallesVenta(false);
                }
            } catch (SQLException ex) {
                manejarErrorGUI(ex);
            }
        });

        HBox contenido = new HBox(18, form, tabla);
        HBox.setHgrow(tabla, Priority.ALWAYS);
        panel.setCenter(contenido);
        return panel;
    }

    // ==================== CARGA DE DATOS DESDE LOS DAO ====================

    private void cargarCategorias(boolean mostrarErrorPopup) {
        try {
            List<CategoriaDTO> lista = categoriaDao.listar();
            listaCategorias.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay categorías registradas." : "Categorías listadas: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarProductos(boolean mostrarErrorPopup) {
        try {
            List<ProductoDTO> lista = productoDao.listar();
            listaProductos.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay productos registrados." : "Productos listados: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarCiudades(boolean mostrarErrorPopup) {
        try {
            List<CiudadDTO> lista = ciudadDao.listar();
            listaCiudades.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay ciudades registradas." : "Ciudades listadas: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarClientes(boolean mostrarErrorPopup) {
        try {
            List<ClienteDTO> lista = clienteDao.listar();
            listaClientes.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay clientes registrados." : "Clientes listados: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarVendedores(boolean mostrarErrorPopup) {
        try {
            List<VendedorDTO> lista = vendedorDao.listar();
            listaVendedores.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay vendedores registrados." : "Vendedores listados: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarVentas(boolean mostrarErrorPopup) {
        try {
            List<VentaDTO> lista = ventaDao.listar();
            listaVentas.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay ventas registradas." : "Ventas listadas: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    private void cargarDetallesVenta(boolean mostrarErrorPopup) {
        try {
            List<DetalleVentaDTO> lista = detalleVentaDao.listar();
            listaDetalles.setAll(lista);
            actualizarEstado(lista.isEmpty() ? "No hay detalles de venta registrados." : "Detalles de venta listados: " + lista.size());
        } catch (SQLException e) {
            if (mostrarErrorPopup) manejarErrorGUI(e);
            else actualizarEstado(obtenerMensajeErrorSQL(e));
        }
    }

    // ==================== MANEJO DE ERRORES Y UTILIDADES ====================

    public static String obtenerMensajeErrorSQL(SQLException e) {
        return switch (e.getErrorCode()) {
            case 1045 -> "Error: usuario o contraseña de base de datos incorrectos.";
            case 1044, 1142, 1143 -> "Permiso denegado: tu usuario no tiene autorización para realizar esta operación.";
            case 1049 -> "Error: la base de datos no existe.";
            case CategoriaDAO.ERR_DUPLICADO -> "Error: ya existe un registro con ese valor único o clave primaria.";
            case CategoriaDAO.ERR_EN_USO -> "Error: no se puede eliminar, existen dependencias asociadas a este registro.";
            case 1452 -> "Error de llave foránea: el ID relacionado (categoría, ciudad, cliente, vendedor, venta o producto) no existe.";
            case 4025 -> "Error de validación (CHECK): verifica que la cantidad sea mayor a 0.";
            default -> {
                String estado = e.getSQLState();
                if (e.getMessage() != null && e.getMessage().contains("No suitable driver")) {
                    yield "Error: falta el driver de MariaDB en el proyecto.";
                } else if (estado != null && estado.startsWith("08")) {
                    yield "Error: no se pudo conectar. ¿Está iniciado MySQL en XAMPP?";
                } else {
                    yield "Error de base de datos (" + e.getErrorCode() + "): " + e.getMessage();
                }
            }
        };
    }

    private void manejarErrorGUI(SQLException e) {
        String mensaje = obtenerMensajeErrorSQL(e);
        actualizarEstado(mensaje);
        mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", mensaje);
    }

    private void actualizarEstado(String texto) {
        if (lblEstadoGlobal != null) {
            lblEstadoGlobal.setText(texto);
        }
    }

    private Integer validarEntero(String valor, String campo) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Número Inválido",
                    "Ingresa un número entero válido en: " + campo + ".");
            return null;
        }
    }

    private Double validarDecimal(String valor, String campo) {
        try {
            return Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Número Inválido",
                    "Ingresa un número decimal válido en: " + campo + ".");
            return null;
        }
    }

    private Integer obtenerIdSeleccionadoOBuscado(String txtFormId, String txtBuscarId, String mensajeFalta) {
        String fuente = !txtFormId.trim().isEmpty() ? txtFormId.trim() : txtBuscarId.trim();
        if (fuente.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID Requerido", mensajeFalta);
            return null;
        }
        return validarEntero(fuente, "ID");
    }

    private boolean confirmarAccion(String titulo, String pregunta) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(pregunta);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private VBox crearTarjetaFormulario(String titulo) {
        VBox box = new VBox(10);
        box.setPrefWidth(295);
        box.setPadding(new Insets(18));
        box.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #E0D4C8; " +
                "-fx-border-radius: 10;"
        );

        Label lbl = new Label(titulo);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 15));
        lbl.setTextFill(Color.web("#2C1A12"));
        box.getChildren().addAll(lbl, new Separator());
        return box;
    }

    private Button estilizarBoton(String texto, String colorHex) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + colorHex + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 6; " +
                "-fx-padding: 7 12; " +
                "-fx-cursor: hand;"
        );
        return btn;
    }
}
