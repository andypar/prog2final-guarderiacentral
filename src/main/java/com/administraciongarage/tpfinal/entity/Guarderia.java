package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="guarderia")
public class Guarderia {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "guarderia_id")
    private List<Zona> zonas;

    public Guarderia() {
        this.zonas = new ArrayList<Zona>();
    }

    @Override
    public String toString() {
        return "Guarderia [zonas=" + zonas + "]";
    }

}
