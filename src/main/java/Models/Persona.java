/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.time.LocalDateTime;

/**
 *
 * @author Bryan
 */
public class Persona {

    private String id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Persona(String id, String nombres, String apellidos, String telefono, String direccion, LocalDateTime createdAt ,LocalDateTime updatedAt) {
        this.id = id;
        this.nombres = nombres.toLowerCase();
        this.apellidos = apellidos.toLowerCase();
        this.telefono = telefono.toLowerCase();
        this.direccion = direccion.toLowerCase();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Persona(String nombres, String apellidos, String telefono, String direccion, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.nombres = nombres.toLowerCase();
        this.apellidos = apellidos.toLowerCase();
        this.telefono = telefono.toLowerCase();
        this.direccion = direccion.toLowerCase();       
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    } 
        
    //SETTERS Y GETTERS
    public Persona() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.toLowerCase();
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres.toLowerCase();
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos.toLowerCase();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono.toLowerCase();
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion.toLowerCase();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Sobreescribiendo el metodo toString()
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", telefono=" + telefono + ", direccion=" + direccion + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
