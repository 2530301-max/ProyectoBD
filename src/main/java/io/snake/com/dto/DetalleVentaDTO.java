package io.snake.com.dto;

public class DetalleVentaDTO {
    private int idVenta;
    private int idProducto;
    private int cantidad;

    public DetalleVentaDTO() { }

    public DetalleVentaDTO(int idVenta, int idProducto, int cantidad) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return String.format("Venta ID: %-6d | Producto ID: %-4d | Cantidad: %-4d",
                idVenta, idProducto, cantidad);
    }
}
