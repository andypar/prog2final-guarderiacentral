package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="zona")
public class Zona {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String letra;
    @Column(name = "tipo_vehiculos")
    private String tipoVehiculos;
    @Column(name = "numero_vehiculos")
    private Integer numeroVehiculos;
    @Column(name = "profundidad_garage")
    private Float profundidadGarage;
    @Column(name = "ancho_garage")
    private Float anchoGarage;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private List<Garage> garages;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private List<ZonaAsignada> zonasAsignadas;

    public Zona(String letra, String tipoVehiculos, Integer numeroVehiculos, Float profundidadGarage,
                Float anchoGarage) {
        super();
        this.letra = letra;
        this.tipoVehiculos = tipoVehiculos;
        this.numeroVehiculos = numeroVehiculos;
        this.profundidadGarage = profundidadGarage;
        this.anchoGarage = anchoGarage;
        this.garages = new ArrayList<>();
        this.zonasAsignadas = new ArrayList<>();
    }

    public Zona() {
    }


    @Override
    public String toString() {
        return "\t Letra: " + letra + "\n\t Tipo de Vehiculos: " + tipoVehiculos + "\n\t Numero de Vehiculos: " + numeroVehiculos
                + "\n\t Profundidad del Garage: " + profundidadGarage + "\n\t Ancho del Garage: " + anchoGarage;
    }

}
