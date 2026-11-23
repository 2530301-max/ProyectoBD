package io.snake.com.dto;

public class ClienteDTO {
    private int idCliente;
    private String nombre;
    private String email;
    private int idCiudad;

    public ClienteDTO() { }

    public ClienteDTO(String nombre, String email, int idCiudad) {
        this.nombre = nombre;
        this.email = email;
        this.idCiudad = idCiudad;
    }

    public ClienteDTO(int idCliente, String nombre, String email, int idCiudad) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.idCiudad = idCiudad;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getIdCiudad() { return idCiudad; }
    public void setIdCiudad(int idCiudad) { this.idCiudad = idCiudad; }

    @Override
    public String toString() {
        return String.format("ID: %-4d | Cliente: %-20s | Email: %-22s | Ciudad ID: %-3d",
                idCliente, nombre, email, idCiudad);
    }
}
