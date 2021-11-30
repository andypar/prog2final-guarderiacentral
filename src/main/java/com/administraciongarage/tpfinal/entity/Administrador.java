package com.administraciongarage.tpfinal.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="administrador")
public class Administrador extends Usuario {

    @Override
    public String getVista() {
        return "menu-admin";
    }
}
