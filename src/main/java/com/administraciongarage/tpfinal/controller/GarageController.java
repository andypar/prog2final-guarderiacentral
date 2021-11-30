package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.Garage;
import com.administraciongarage.tpfinal.entity.Vehiculo;
import com.administraciongarage.tpfinal.entity.Zona;
import com.administraciongarage.tpfinal.repository.GarageRepository;
import com.administraciongarage.tpfinal.repository.PropiedadGarageRepository;
import com.administraciongarage.tpfinal.repository.VehiculoRepository;
import com.administraciongarage.tpfinal.repository.ZonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class GarageController {

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private PropiedadGarageRepository propiedadGarageRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @RequestMapping(value = "/garages", method = RequestMethod.GET)
    public String listarGarages(Model model) {

        model.addAttribute("garages", garageRepository.obtenerTodas());
        return "garage-list";
    }

    @RequestMapping(value = "/garage/crear", method = RequestMethod.GET)
    public String crearGarage(Model model) {

        List<Zona>zonaslibres= zonaRepository.obtenerTodas();
        if(zonaslibres.size()==0){
            model.addAttribute("error", "No hay zonas disponibles");
            return "error";
        }
        model.addAttribute("zonas",zonaslibres);
        return "garage-add";

    }

    @RequestMapping(value = "/garage/crear", method = RequestMethod.POST)
    public String crearGarage(@RequestParam Integer numero, @RequestParam Integer zonaId,@RequestParam Float lecturaContadorLuz, @RequestParam Boolean tieneServiciosMantenimiento, Model model) {
        Garage v = new Garage();
        Zona z = zonaRepository.buscar(zonaId);

        if(numero<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        if(garageRepository.buscarGaragePorNumero(numero)!=null){
            model.addAttribute("error", "Numero de garage ya existe!!");
            return "error";
        }
        v.setNumero(numero);

        if(lecturaContadorLuz<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setLecturaContadorLuz(lecturaContadorLuz);

        v.setTieneServiciosMantenimiento(tieneServiciosMantenimiento);
        v.setVehiculo(null);
        v.setPropiedad(null);
        garageRepository.crear(v);
        zonaRepository.agregarGarage(zonaId,v);

        model.addAttribute("garages", garageRepository.obtenerTodas());
        return "garage-list";
    }

    @RequestMapping(value = "/garage/eliminar", method = RequestMethod.POST)
    public String eliminarGarage(@RequestParam Integer id, Model model) {

        List<Garage> garages = propiedadGarageRepository.buscarGarageId(id);
        if(garages.size()>0 ){
            model.addAttribute("error", "Garage se encuentra asignado a un socio. No se puede eliminar");
            return "error";
        }
        Vehiculo vehiculo = vehiculoRepository.buscarGarageId(id);
        if(vehiculo != null ){
            model.addAttribute("error", "Garage se encuentra asignado a un vehiculo. No se puede eliminar");
            return "error";
        }

        garageRepository.eliminar(id);
        model.addAttribute("garages", garageRepository.obtenerTodas());
        return "garage-list";
    }

    @RequestMapping(value = "/garage/modificar", method = RequestMethod.POST)
    public String modificarGarage(@RequestParam Integer id, Model model) {

        List<Zona> zonas = zonaRepository.obtenerTodas();
        Garage g = garageRepository.buscar(id);
        Zona zonaSeleccionada = zonaRepository.buscarGarageId(g.getId());

        model.addAttribute("zonas", zonas);
        model.addAttribute("zonaseleccionada", zonaSeleccionada );
        model.addAttribute("garage", garageRepository.buscar(id));
        return "garage-update";
    }

    @RequestMapping(value = "/garage/guardar_modificacion", method = RequestMethod.POST)
    public String modificarGarage(@RequestParam Integer id,@RequestParam Integer zonaId,@RequestParam Integer numero,@RequestParam Float lecturaContadorLuz, @RequestParam Boolean tieneServiciosMantenimiento, Model model) {
        Garage v = garageRepository.buscar(id);

        Garage garageEncontrado = garageRepository.buscarGaragePorNumero(numero);
        if(numero<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        if(garageEncontrado !=null && garageEncontrado.getId() != id){
            model.addAttribute("error", "Numero de garage ya existe!!");
            return "error";
        }
        v.setNumero(numero);

        if(lecturaContadorLuz<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setLecturaContadorLuz(lecturaContadorLuz);
        v.setTieneServiciosMantenimiento(tieneServiciosMantenimiento);
        v.setVehiculo(null);
        v.setPropiedad(null);
        garageRepository.modificar(v);
        zonaRepository.agregarGarage(zonaId,v);

        model.addAttribute("garages", garageRepository.obtenerTodas());
        return "garage-list";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }

}
