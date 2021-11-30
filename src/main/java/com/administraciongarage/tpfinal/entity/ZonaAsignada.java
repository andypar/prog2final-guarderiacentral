package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="zonaasignada")
public class ZonaAsignada {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(name = "numero_vehiculos")
    private Integer numeroVehiculosACargo;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zona_id")
    private Zona zona;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    public ZonaAsignada() {
    }

    @Override
    public String toString() {
        return "\t Numero de Vehiculos A Cargo: " + numeroVehiculosACargo + "\n\t Zona: " + zona.getLetra() + "\n\t Empleado: " + empleado + "\n";
    }

}

