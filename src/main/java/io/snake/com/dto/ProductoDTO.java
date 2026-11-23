package io.snake.com.dto;

public class ProductoDTO {
    private int idProducto;
    private String nombre;
    private int idCategoria;
    private double precioReferencia;

    public ProductoDTO() { }

    public ProductoDTO(String nombre, int idCategoria, double precioReferencia) {
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.precioReferencia = precioReferencia;
    }

    public ProductoDTO(int idProducto, String nombre, int idCategoria, double precioReferencia) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.precioReferencia = precioReferencia;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public double getPrecioReferencia() { return precioReferencia; }
    public void setPrecioReferencia(double precioReferencia) { this.precioReferencia = precioReferencia; }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Producto: %-22s | Cat ID: %-3d | Precio: $%-8.2f",
                idProducto, nombre, idCategoria, precioReferencia);
    }
}
