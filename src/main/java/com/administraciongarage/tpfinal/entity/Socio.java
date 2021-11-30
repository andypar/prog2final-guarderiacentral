package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="socio")
public class Socio extends Usuario {

    @Column(name = "fecha_ingreso_guarderia")
    private LocalDate fechaIngresoGuarderia;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id")
    private List<Vehiculo> vehiculos;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id")
    private List<PropiedadGarage> propiedades;

    public Socio(LocalDate fechaIngresoGuarderia, String nombre, String direccion, String telefono, Integer dni, String pwd) {
        super(nombre, direccion, telefono, dni, pwd);
        this.fechaIngresoGuarderia = fechaIngresoGuarderia;
        this.vehiculos = new ArrayList<>();
        this.propiedades = new ArrayList<>();
    }

    public Socio() {
    }

    @Override
    public String getVista() {
        return "menu-socio";
    }

}

