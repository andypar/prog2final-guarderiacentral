package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.PropiedadGarage;
import com.administraciongarage.tpfinal.entity.Socio;
import com.administraciongarage.tpfinal.repository.PropiedadGarageRepository;
import com.administraciongarage.tpfinal.repository.SocioRepository;
import com.administraciongarage.tpfinal.entity.Garage;
import com.administraciongarage.tpfinal.repository.GarageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class PropiedadGarageController {

    @Autowired
    private PropiedadGarageRepository propiedadGarageRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private SocioRepository socioRepository;


    @RequestMapping(value = "/socio/propiedades", method = RequestMethod.POST)
    public String garagesLibres(@RequestParam Integer id, Model model) {
        model.addAttribute("garageslibres", propiedadGarageRepository.obtenerLibres());
        model.addAttribute("garagescomprados", propiedadGarageRepository.obtenerPropios(id));

        model.addAttribute("socioId", id);
        return "propiedades-list";
    }

    @RequestMapping(value = "/socio/propiedades/comprar", method = RequestMethod.POST)
    public String comprarPropiedad(@RequestParam Integer id, @RequestParam Integer socioId,Model model) {

        Garage g = garageRepository.buscar(id);

        model.addAttribute("garage", g);
        model.addAttribute("socioId", socioId);
        return "propiedades-add";
    }


    @RequestMapping(value = "/socio/propiedades/guardarcompra", method = RequestMethod.POST)
    public String guardarCompra(@RequestParam Integer id, @RequestParam Integer socioId, @RequestParam String fechaCompra, Model model) {

        LocalDate fechaCompraAux = LocalDate.parse(fechaCompra, DateTimeFormatter.ISO_DATE);

        Garage g = garageRepository.buscar(id);
        Socio s = socioRepository.buscar(socioId);
        PropiedadGarage p = new PropiedadGarage();
        p.setGarage(g);

        if (fechaCompraAux.isAfter(LocalDate.now())){
            model.addAttribute("error", "No puede ingresar una fecha futura");
            return "error";
        }
        p.setFechaCompra(fechaCompraAux);

        p.setSocio(s);
        propiedadGarageRepository.crear(p);
        g.setPropiedad(p);
        garageRepository.modificar(g);
        socioRepository.agregarPropiedad(socioId,p);

        model.addAttribute("socioId", socioId);
        model.addAttribute("garageslibres", propiedadGarageRepository.obtenerLibres());
        model.addAttribute("garagescomprados", propiedadGarageRepository.obtenerPropios(socioId));

        return("propiedades-list");
    }


    @RequestMapping(value = "/socio/propiedades/vender", method = RequestMethod.POST)
    public String venderPropiedad(@RequestParam Integer id, @RequestParam Integer socioId, Model model) {

        Garage g = garageRepository.buscar(id);
        Socio s = socioRepository.buscar(socioId);
        PropiedadGarage p = propiedadGarageRepository.buscar(id,socioId);

        g.setPropiedad(null);
        garageRepository.modificar(g);
        //socioRepository.eliminarPropiedad(socioId,p);
        propiedadGarageRepository.eliminar(p.getId());

        model.addAttribute("socioId", socioId);
        model.addAttribute("garageslibres", propiedadGarageRepository.obtenerLibres());
        model.addAttribute("garagescomprados", propiedadGarageRepository.obtenerPropios(id));

        return("propiedades-list");
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }

}
