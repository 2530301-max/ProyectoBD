package io.snake.com.dto;

import java.time.LocalDate;

public class VentaDTO {
    private int idVenta;
    private LocalDate fechaVenta;
    private int idCliente;
    private String idVendedor;
    private double totalVenta;

    public VentaDTO() { }

    public VentaDTO(LocalDate fechaVenta, int idCliente, String idVendedor, double totalVenta) {
        this.fechaVenta = fechaVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.totalVenta = totalVenta;
    }

    public VentaDTO(int idVenta, LocalDate fechaVenta, int idCliente, String idVendedor, double totalVenta) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.totalVenta = totalVenta;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getIdVendedor() { return idVendedor; }
    public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    @Override
    public String toString() {
        return String.format("Venta ID: %-6d | Fecha: %-10s | Cliente: %-3d | Vendedor: %-6s | Total: $%-8.2f",
                idVenta, fechaVenta, idCliente, idVendedor, totalVenta);
    }
}
