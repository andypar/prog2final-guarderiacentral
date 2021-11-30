package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="empleado")
public class Empleado extends Usuario {

    private String codigo;
    private String especialidad;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private List<ZonaAsignada> zonaEmpleado;

    public Empleado(String nombre, String direccion, String telefono, Integer dni, String pwd) {
        super(nombre, direccion, telefono, dni, pwd);
    }

    public Empleado(String codigo, String especialidad, String nombre, String direccion, String telefono, Integer dni, String password) {
        super(nombre, direccion, telefono, dni, password);
        this.codigo = codigo;
        this.especialidad = especialidad;
        this.zonaEmpleado = new ArrayList<>();
    }

    public Empleado() {
    }

    @Override
    public String getVista() {
        return "menu-empleado";
    }

    @Override
    public String toString() {

        return super.toString() + "\n\t Codigo: " + codigo + "\n\t Especialidad: " + especialidad;
    }

}

