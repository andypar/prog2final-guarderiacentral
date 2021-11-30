package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name="propiedadgarage")
public class PropiedadGarage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private Garage garage;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    public PropiedadGarage(LocalDate fechaCompra, Garage garage, Socio socio) {

        this.fechaCompra = fechaCompra;
        this.garage = garage;
        this.socio = socio;

    }

    public PropiedadGarage() {

    }

    @Override
    public String toString() {
        return "\n\t Fecha de Compra: " + fechaCompra + "\n\t Garage: " + garage;
    }

}
