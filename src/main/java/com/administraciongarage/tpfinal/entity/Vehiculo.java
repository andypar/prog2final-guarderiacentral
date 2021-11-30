package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String matricula;
    private String marca;
    private String tipo;
    private Float largo;
    private Float ancho;
    @Column(name = "fecha_asignacion_garage")
    private LocalDate fechaAsignacionGarage;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "garage_id")
    private Garage garage;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    public Vehiculo(String matricula, String nombre, String tipo, Float largo, Float ancho) {

        this.matricula = matricula;
        this.marca = nombre;
        this.tipo = tipo;
        this.largo = largo;
        this.ancho = ancho;
    }

    public Vehiculo() {
    }


    @Override
    public String toString() {
        return "\n\t Matricula: " + matricula + "\n\t Nombre: " + marca + "\n\t Tipo: " + tipo + "\n\t Largo: " + largo + "\n\t Ancho: " + ancho + "\n\t Fecha de Asignacion a Garage: " + fechaAsignacionGarage;
    }

}
