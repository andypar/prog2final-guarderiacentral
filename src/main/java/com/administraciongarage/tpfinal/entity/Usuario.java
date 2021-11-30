package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private Integer dni;
    private String user;
    private String pwd;

    public Usuario(String nombre, String direccion, String telefono, Integer dni, String pwd) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dni = dni;
        this.pwd = pwd;
    }

    public Usuario() {
    }

    public abstract String getVista();

    @Override
    public String toString() {
        return "\t Nombre: " + nombre + "\n\t Direccion: " + direccion + "\n\t Telefono: " + telefono + "\n\t DNI: " + dni + "\n\t Password: " + pwd;
    }
}


