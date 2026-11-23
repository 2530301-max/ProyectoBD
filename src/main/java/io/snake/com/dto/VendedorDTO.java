package io.snake.com.dto;

public class VendedorDTO {
    private String idVendedor;
    private String nombre;

    public VendedorDTO() { }

    public VendedorDTO(String idVendedor, String nombre) {
        this.idVendedor = idVendedor;
        this.nombre = nombre;
    }

    public String getIdVendedor() { return idVendedor; }
    public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return String.format("ID: %-6s | Vendedor: %s", idVendedor, nombre);
    }
}
