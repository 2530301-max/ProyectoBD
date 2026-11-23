package io.snake.com.dto;

public class CategoriaDTO {
    private int id;
    private String nombre;

    public CategoriaDTO() { }

    public CategoriaDTO(String nombre) {
        this.nombre = nombre;
    }

    public CategoriaDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return String.format("%-5d %s", id, nombre);
    }
}
