package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="garage")
public class Garage  {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private Integer numero;
    @Column(name = "lectura_contador_luz")
    private Float lecturaContadorLuz;
    @Column(name = "tiene_servicios_mantenimiento")
    private Boolean tieneServiciosMantenimiento;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_garage_id")
    private PropiedadGarage propiedad;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zona_id", table = "garage")
    private Zona zona;

    public Garage(Integer numero, Float lecturaContadorLuz, Boolean tieneServiciosMantenimiento) {
        super();
        this.numero = numero;
        this.lecturaContadorLuz = lecturaContadorLuz;
        this.tieneServiciosMantenimiento = tieneServiciosMantenimiento;
        this.vehiculo = vehiculo;
        this.propiedad = propiedad;
    }

    public Garage() {
    }

    @Override
    public String toString() {
        return "\n\t Numero: " + numero + "\n\t Lectura del Contador de Luz: " + lecturaContadorLuz + "\n\t Tiene Servicios de Mantenimiento: " + (tieneServiciosMantenimiento ? "s" : "n") ;
    }
}
