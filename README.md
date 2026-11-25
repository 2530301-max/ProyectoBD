# Sistema de Gestión de Cafetería (SNAKE) - JavaFX & MariaDB

Aplicación de escritorio desarrollada en **Java 21** con interfaz gráfica en **JavaFX** y persistencia de datos en **MariaDB / MySQL** utilizando el patrón **DAO / DTO** y control de sesión directo con usuarios de base de datos.

---

## 1. Requisitos Previos

Antes de comenzar, asegúrate de contar con las siguientes herramientas instaladas:

- **Java JDK 21** o superior.
- **Apache Maven** (o el Maven integrado en **IntelliJ IDEA**).
- **XAMPP** (con los servicios **Apache** y **MySQL / MariaDB** activos en el puerto `3306`).

---

## 2. Paso a Paso: Importar la Base de Datos en phpMyAdmin

El archivo de la base de datos se encuentra dentro del proyecto en la ruta:
```text
sql/cafeteria.sql
```

Sigue estos pasos para importarlo correctamente:

### Paso 2.1: Iniciar los servicios en XAMPP
1. Abre el **Panel de Control de XAMPP**.
2. Haz clic en **Start** junto al módulo **Apache**.
3. Haz clic en **Start** junto al módulo **MySQL** (verifica que marque el puerto `3306`).

### Paso 2.2: Crear la base de datos `cafeteria`
1. Abre tu navegador web e ingresa a:  
   `http://localhost/phpmyadmin`
2. En el panel lateral izquierdo, haz clic en **Nueva** (*New*).
3. En el campo **Nombre de la base de datos**, escribe exactamente:
   ```text
   cafeteria
   ```
4. En el desplegable de cotejamiento (*Collation*), selecciona **`utf8mb4_unicode_ci`**.
5. Haz clic en el botón **Crear**.

### Paso 2.3: Importar el archivo `sql/cafeteria.sql`
1. Asegúrate de tener seleccionada la base de datos **`cafeteria`** en el panel izquierdo de phpMyAdmin.
2. En la barra de pestañas superior, haz clic en **Importar** (*Import*).
3. En la sección **Archivo a importar**, haz clic en **Seleccionar archivo** (*Choose File*).
4. Busca la carpeta del proyecto y selecciona el archivo:
   ```text
   C:\POO\SNAKE\sql\cafeteria.sql
   ```
5. Desplázate hasta el final de la página y haz clic en el botón **Importar** (o **Continuar**).
6. Verás un mensaje de confirmación en verde indicando que la importación se ejecutó exitosamente y aparecerán las **7 tablas** (`categorias`, `ciudades`, `clientes`, `detalle_venta`, `productos`, `vendedores`, `ventas`) y la vista (`v_verificacion_totales`).

---

## 3. Configuración de Usuarios de Base de Datos (Opcional en nueva instalación)

El sistema inicia sesión conectándose directamente con los usuarios de MariaDB/MySQL configurados para la base de datos `cafeteria`. Si estás configurando el proyecto en una computadora nueva y aún no has creado los usuarios (`user_lectura`, `user_estructura`, `user_completo`), puedes crearlos en **phpMyAdmin** yendo a la pestaña **SQL** y ejecutando:

```sql
-- 1. Usuario de solo lectura (SELECT)
CREATE USER IF NOT EXISTS 'user_lectura'@'localhost' IDENTIFIED BY 'tu_password';
GRANT SELECT ON cafeteria.* TO 'user_lectura'@'localhost';

-- 2. Usuario de estructura
CREATE USER IF NOT EXISTS 'user_estructura'@'localhost' IDENTIFIED BY 'tu_password';
GRANT SELECT, CREATE, ALTER, INDEX ON cafeteria.* TO 'user_estructura'@'localhost';

-- 3. Usuario con permisos completos (CRUD)
CREATE USER IF NOT EXISTS 'user_completo'@'localhost' IDENTIFIED BY 'tu_password';
GRANT ALL PRIVILEGES ON cafeteria.* TO 'user_completo'@'localhost';

FLUSH PRIVILEGES;
```
*(También puedes iniciar sesión en la aplicación utilizando el usuario `root` si tu entorno local de XAMPP lo permite).*

---

## 4. Paso a Paso: Compilar y Ejecutar el Proyecto

Puedes compilar y ejecutar el proyecto desde **IntelliJ IDEA** o desde la **Terminal con Maven**.

### Opción A: Desde IntelliJ IDEA (Recomendado)

1. Abre **IntelliJ IDEA** y selecciona **File → Open...**.
2. Selecciona la carpeta raíz del proyecto (`C:\POO\SNAKE`).
3. Si aparece el icono flotante de **Maven** (o en la barra lateral derecha **Maven**), haz clic en **Reload All Maven Projects** para cargar las dependencias de **MariaDB** y **JavaFX 21**.
4. En el árbol del proyecto, abre el archivo:
   ```text
   src/main/java/io/snake/com/LoginApp.java
   ```
5. Haz clic en el botón verde **Run (`▶`)** junto al método `main` de `LoginApp` (o de `Main.java`).
6. Se abrirá la ventana de **Inicio de Sesión en JavaFX**, donde podrás elegir o escribir tu usuario de BD, ingresar su contraseña y acceder a las 7 pestañas de gestión (**Categorías**, **Productos**, **Ciudades**, **Clientes**, **Vendedores**, **Ventas** y **Detalle de Venta**).

---

### Opción B: Desde la Terminal con Maven

1. Abre una terminal (PowerShell o CMD) en la carpeta raíz del proyecto:
   ```powershell
   cd C:\POO\SNAKE
   ```
2. **Para compilar el proyecto:**
   ```powershell
   mvn clean compile
   ```
3. **Para ejecutar la interfaz gráfica JavaFX:**
   ```powershell
   mvn javafx:run
   ```

---

### Opción C: Ejecutar en Modo Consola (Opcional)

Si deseas usar el menú clásico por consola en lugar de la interfaz gráfica, puedes pasar el argumento `--consola` a `Main`:
- En IntelliJ IDEA: Edita la configuración de ejecución de `Main` y agrega `--consola` en **Program arguments**.

---

## 5. Estructura del Proyecto

```text
SNAKE/
├── pom.xml                           # Configuración de Maven (MariaDB + JavaFX 21)
├── README.md                         # Guía de instalación, importación y compilación
├── sql/
│   └── cafeteria.sql                 # Script SQL con estructura y datos de la BD
└── src/main/java/io/snake/com/
    ├── LoginApp.java                 # Punto de entrada principal (Lanzador JavaFX)
    ├── Main.java                     # Lanzador secundario / Modo consola (--consola)
    ├── config/
    │   └── ConexionDB.java           # Gestión de conexión JDBC y sesión activa
    ├── models/
    │   └── Categoria.java            # Modelo de entidad
    ├── dto/                          # Objetos de Transferencia de Datos (DTO)
    │   ├── CategoriaDTO.java
    │   ├── CiudadDTO.java
    │   ├── ClienteDTO.java
    │   ├── DetalleVentaDTO.java
    │   ├── ProductoDTO.java
    │   ├── VendedorDTO.java
    │   └── VentaDTO.java
    ├── dao/                          # Objetos de Acceso a Datos (DAO - CRUD SQL)
    │   ├── CategoriaDAO.java
    │   ├── CiudadDAO.java
    │   ├── ClienteDAO.java
    │   ├── DetalleVentaDAO.java
    │   ├── ProductoDAO.java
    │   ├── VendedorDAO.java
    │   └── VentaDAO.java
    └── ui/
        └── CafeteriaFX.java          # Interfaz gráfica JavaFX (Login + 7 pestañas CRUD)
```
