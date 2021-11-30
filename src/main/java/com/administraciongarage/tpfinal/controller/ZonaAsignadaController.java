package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.*;
import com.administraciongarage.tpfinal.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class ZonaAsignadaController {

    @Autowired
    private ZonaAsignadaRepository zonaAsignadaRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @RequestMapping(value = "/empleado/zonas", method = RequestMethod.POST)
    public String garagesLibres(@RequestParam Integer id, Model model) {
        model.addAttribute("zonasdisponibles", zonaAsignadaRepository.obtenerZonas(id));
        model.addAttribute("zonasasignadas", zonaAsignadaRepository.obtenerZonasAsignadas(id));

        model.addAttribute("empleadoId", id);
        return "zonasasignadas-list";
    }

    @RequestMapping(value = "/empleado/zonas/asignar", method = RequestMethod.POST)
    public String asignarZona(@RequestParam Integer id, @RequestParam Integer empleadoId, Model model) {

        Zona z = zonaRepository.buscar(id);

        model.addAttribute("zona", z);
        model.addAttribute("empleadoId", empleadoId);
        return "zonasasignadas-add";
    }


    @RequestMapping(value = "/empleado/zonas/guardarzona", method = RequestMethod.POST)
    public String guardarAsignacion(@RequestParam Integer id, @RequestParam Integer empleadoId, @RequestParam Integer numeroVehiculosACargo, Model model) {

        Zona z = zonaRepository.buscar(id);
        Empleado e = empleadoRepository.buscar(empleadoId);
        ZonaAsignada za = new ZonaAsignada();
        za.setZona(z);

        if(numeroVehiculosACargo<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        za.setNumeroVehiculosACargo(numeroVehiculosACargo);

        za.setEmpleado(e);
        zonaAsignadaRepository.crear(za);
        zonaRepository.modificar(z);

        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("zonasdisponibles", zonaAsignadaRepository.obtenerZonas(empleadoId));
        model.addAttribute("zonasasignadas", zonaAsignadaRepository.obtenerZonasAsignadas(empleadoId));

        return("zonasasignadas-list");
    }


    @RequestMapping(value = "/empleado/zonas/desasignar", method = RequestMethod.POST)
    public String desasignarZona(@RequestParam Integer id, @RequestParam Integer empleadoId, Model model) {

        Zona z = zonaRepository.buscar(id);
        Empleado e = empleadoRepository.buscar(empleadoId);
        ZonaAsignada za = zonaAsignadaRepository.buscar(id, empleadoId);

        zonaAsignadaRepository.eliminar(za.getId());

        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("zonasdisponibles", zonaAsignadaRepository.obtenerZonas(empleadoId));
        model.addAttribute("zonasasignadas", zonaAsignadaRepository.obtenerZonasAsignadas(empleadoId));

        return("zonasasignadas-list");
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }

}
