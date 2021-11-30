package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.Zona;
import com.administraciongarage.tpfinal.repository.ZonaAsignadaRepository;
import com.administraciongarage.tpfinal.repository.ZonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ZonaController {

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ZonaAsignadaRepository zonaAsignadaRepository;

    @RequestMapping(value = "/zonas", method = RequestMethod.GET)
    public String listarZonas(Model model) {

        model.addAttribute("zonas", zonaRepository.obtenerTodas());
        return "zona-list";
    }

    @RequestMapping(value = "/zona/crear", method = RequestMethod.GET)
    public String crearZona() {
        return "zona-add";
    }

    @RequestMapping(value = "/zona/crear", method = RequestMethod.POST)
    public String crearZona(@RequestParam String letra, @RequestParam String tipoVehiculos, @RequestParam Integer numeroVehiculos, @RequestParam Float profundidadGarage, @RequestParam Float anchoGarage, Model model) {
        Zona v = new Zona();

        if(zonaRepository.buscarZonaPorLetra(letra)!=null){
            model.addAttribute("error", "Letra de Zona ya existe!!");
            return "error";
        }
        v.setLetra(letra);
        v.setTipoVehiculos(tipoVehiculos);

        if(numeroVehiculos<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setNumeroVehiculos(numeroVehiculos);
        if(profundidadGarage<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setProfundidadGarage(profundidadGarage);
        if(anchoGarage<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setAnchoGarage(anchoGarage);

        zonaRepository.crear(v);
        model.addAttribute("zonas", zonaRepository.obtenerTodas());
        return "zona-list";
    }

    @RequestMapping(value = "/zona/eliminar", method = RequestMethod.POST)
    public String eliminarZona(@RequestParam Integer id, Model model) {

        List<Zona> zonas = zonaAsignadaRepository.buscarZonaConEmpleados(id);
        if(zonas.size()>0 ){
            model.addAttribute("error", "Zona se encuentra asignada a empleados. No se puede eliminar");
            return "error";
        }

        List<Zona> zonasConGarages = zonaAsignadaRepository.buscarZonasConGarages(id);
        if(zonasConGarages.size()>0 ){
            model.addAttribute("error", "Zona tiene garages asignados. No se puede eliminar");
            return "error";
        }

        zonaRepository.eliminar(id);
        model.addAttribute("zonas", zonaRepository.obtenerTodas());
        return "zona-list";
    }

    @RequestMapping(value = "/zona/modificar", method = RequestMethod.POST)
    public String modificarZona(@RequestParam Integer id, Model model) {

        model.addAttribute("zona", zonaRepository.buscar(id));
        return "zona-update";
    }

    @RequestMapping(value = "/zona/guardar_modificacion", method = RequestMethod.POST)
    public String modificarZona(@RequestParam Integer id,@RequestParam String letra, @RequestParam String tipoVehiculos, @RequestParam Integer numeroVehiculos, @RequestParam Float profundidadGarage, @RequestParam Float anchoGarage, Model model) {
        Zona v = zonaRepository.buscar(id);

        Zona zonaEncontrada = zonaRepository.buscarZonaPorLetra(letra);

        if(zonaEncontrada !=null && zonaEncontrada.getId() != id){
            model.addAttribute("error", "Letra de Zona ya existe!!");
            return "error";
        }
        v.setLetra(letra);
        v.setTipoVehiculos(tipoVehiculos);

        if(numeroVehiculos<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setNumeroVehiculos(numeroVehiculos);
        if(profundidadGarage<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setProfundidadGarage(profundidadGarage);
        if(anchoGarage<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setAnchoGarage(anchoGarage);

        //v.setZonasAsignadas(new ArrayList<>());
        //v.setGarages(new ArrayList<>());
        zonaRepository.modificar(v);

        model.addAttribute("zonas", zonaRepository.obtenerTodas());
        return "zona-list";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }

}
