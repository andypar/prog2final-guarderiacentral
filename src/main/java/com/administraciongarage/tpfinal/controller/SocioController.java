package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.*;
import com.administraciongarage.tpfinal.repository.PropiedadGarageRepository;
import com.administraciongarage.tpfinal.repository.SocioRepository;
import com.administraciongarage.tpfinal.repository.UsuarioRepository;
import com.administraciongarage.tpfinal.repository.VehiculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@Slf4j
public class SocioController {

    @Autowired
    private SocioRepository socioRepository;
    @Autowired
    private PropiedadGarageRepository propiedadGarageRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


    @RequestMapping(value = "/menu-socio", method = RequestMethod.POST)
    public String menuSocio(@RequestParam Integer id, Model model) {

        model.addAttribute("id",id);
        return "menu-socio";
    }

    @RequestMapping(value = "/socio-datos", method = RequestMethod.POST)
    public String verDatos(@RequestParam Integer id,Model model) {

        model.addAttribute("socio", socioRepository.buscar(id));
        model.addAttribute("garagescomprados", propiedadGarageRepository.obtenerPropios(id));
        model.addAttribute("vehiculoscomprados", vehiculoRepository.obtenerPropios(id));
        return "socio-datos";
    }

    @RequestMapping(value = "/socios", method = RequestMethod.GET)
    public String listarSocios(Model model) {
        model.addAttribute("socios", socioRepository.obtenerTodos());
        return "socio-list";
    }

    @RequestMapping(value = "/socio/crear", method = RequestMethod.GET)
    public String crearSocio() {
        return "socio-add";
    }

    @RequestMapping(value = "/socio/crear", method = RequestMethod.POST)
    public String crearSocio(@RequestParam String fechaIngresoGuarderia, @RequestParam String nombre, @RequestParam String direccion, @RequestParam String telefono, @RequestParam Integer dni,@RequestParam String user, @RequestParam String pwd, Model model) {

        LocalDate fechaIngresoGuarderiaAux = LocalDate.parse(fechaIngresoGuarderia, DateTimeFormatter.ISO_DATE);

        Socio v = new Socio();

        if (fechaIngresoGuarderiaAux.isAfter(LocalDate.now())){
            model.addAttribute("error", "No puede ingresar una fecha futura");
            return "error";
        }
        v.setFechaIngresoGuarderia(fechaIngresoGuarderiaAux);

        v.setNombre(nombre);
        v.setDireccion(direccion);
        v.setTelefono(telefono);

        if(dni <=0){
            model.addAttribute("error", "DNI Invalido. Debe ser mayor a 0");
            return "error";
        }
        if(usuarioRepository.buscarUsuarioPorDNI(dni)!=null){
            model.addAttribute("error", "DNI de socio ya existe!!");
            return "error";
        }
        v.setDni(dni);

        if(usuarioRepository.buscarUsuarioPorUser(user)!=null){
            model.addAttribute("error", "Identificador de Usuario no disponible!!");
            return "error";
        }
        v.setUser(user);

        v.setPwd(pwd);
        v.setVehiculos(null);
        v.setPropiedades(null);
        socioRepository.crear(v);
        model.addAttribute("socios", socioRepository.obtenerTodos());
        return "socio-list";
    }

    @RequestMapping(value = "/socio/eliminar", method = RequestMethod.POST)
    public String eliminarSocio(@RequestParam Integer id, Model model) {

        List<Garage> garages = propiedadGarageRepository.obtenerPropios(id);
        if(garages.size()>0 ){
            model.addAttribute("error", "Socio tiene una propiedad asignada. No se puede eliminar");
            return "error";
        }

        List<Vehiculo> vehiculos = vehiculoRepository.obtenerPropios(id);
        if(vehiculos.size()>0 ){
            model.addAttribute("error", "Socio tiene un vehiculo asignado. No se puede eliminar");
            return "error";
        }

        socioRepository.eliminar(id);
        model.addAttribute("socios", socioRepository.obtenerTodos());
        return "socio-list";
    }

    @RequestMapping(value = "/socio/modificar", method = RequestMethod.POST)
    public String modificarSocio(@RequestParam Integer id, Model model) {

        model.addAttribute("socio", socioRepository.buscar(id));
        return "socio-update";
    }

    @RequestMapping(value = "/socio/guardar_modificacion", method = RequestMethod.POST)
    public String modificarSocio(@RequestParam Integer id, @RequestParam String fechaIngresoGuarderia, @RequestParam String nombre, @RequestParam String direccion, @RequestParam String telefono, @RequestParam Integer dni, @RequestParam String user, @RequestParam String pwd, Model model) {

        LocalDate fechaIngresoGuarderiaAux = LocalDate.parse(fechaIngresoGuarderia, DateTimeFormatter.ISO_DATE);

        Socio v = socioRepository.buscar(id);
        if (fechaIngresoGuarderiaAux.isAfter(LocalDate.now())){
            model.addAttribute("error", "No puede ingresar una fecha futura");
            return "error";
        }
        v.setFechaIngresoGuarderia(fechaIngresoGuarderiaAux);

        v.setNombre(nombre);
        v.setDireccion(direccion);
        v.setTelefono(telefono);

        if(dni <=0){
            model.addAttribute("error", "DNI Invalido. Debe ser mayor a 0");
            return "error";
        }
        Usuario usuarioEncontradoDni = usuarioRepository.buscarUsuarioPorDNI(dni);
        if(usuarioEncontradoDni !=null && usuarioEncontradoDni.getId() != id){
            model.addAttribute("error", "DNI de socio ya existe!!");
            return "error";
        }
        v.setDni(dni);

        Usuario usuarioEncontradoUser = usuarioRepository.buscarUsuarioPorUser(user);
        if(usuarioEncontradoUser !=null && usuarioEncontradoUser.getId() != id){
            model.addAttribute("error", "Identificador de Usuario no disponible!!");
            return "error";
        }
        v.setUser(user);

        v.setPwd(pwd);
        v.setVehiculos(null);
        v.setPropiedades(null);
        socioRepository.modificar(v);

        model.addAttribute("socios", socioRepository.obtenerTodos());
        return "socio-list";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }
}
