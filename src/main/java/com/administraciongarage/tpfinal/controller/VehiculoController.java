package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.Garage;
import com.administraciongarage.tpfinal.entity.Socio;
import com.administraciongarage.tpfinal.entity.Vehiculo;
import com.administraciongarage.tpfinal.entity.Zona;
import com.administraciongarage.tpfinal.repository.GarageRepository;
import com.administraciongarage.tpfinal.repository.SocioRepository;
import com.administraciongarage.tpfinal.repository.VehiculoRepository;
import com.administraciongarage.tpfinal.repository.ZonaRepository;
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
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private ZonaRepository zonaRepository;


    @RequestMapping(value = "/vehiculos", method = RequestMethod.GET)
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.obtenerTodos());
        return "vehiculo-list";
    }

    @RequestMapping(value = "/vehiculo/crear", method = RequestMethod.GET)
    public String crearVehiculo(Model model) {

        List<Garage>garageslibres = garageRepository.buscarGarageLibres();
        if(garageslibres.size()==0){
            model.addAttribute("error", "No hay garages libres");
            return "error";
        }
        model.addAttribute("garages", garageslibres);

        List<Socio>socios = socioRepository.obtenerTodos();
        if(socios.size()==0){
            model.addAttribute("error", "No hay socios libres");
            return "error";
        }
        model.addAttribute("socios", socios);
        return "vehiculo-add";
    }

    @RequestMapping(value = "/vehiculo/crear", method = RequestMethod.POST)
    public String crearVehiculo(@RequestParam Integer socioId,@RequestParam String fechaAsignacionGarage,@RequestParam String matricula, @RequestParam Integer garageId, @RequestParam String marca, @RequestParam String tipo, @RequestParam Float largo, @RequestParam Float ancho, Model model) {

        LocalDate fechaAsignacionGarageAux = LocalDate.parse(fechaAsignacionGarage, DateTimeFormatter.ISO_DATE);

        Garage g = garageRepository.buscar(garageId);
        Zona z = zonaRepository.buscarGarageId(g.getId());
        Vehiculo v = new Vehiculo();

        if(vehiculoRepository.buscarVehiculPorMatricula(matricula)!=null){
            model.addAttribute("error", "Matricula de Vehiculo ya existe!!");
            return "error";
        }
        v.setMatricula(matricula);
        v.setMarca(marca);

        if(!tipo.equals(z.getTipoVehiculos())){
            model.addAttribute("error", "El tipo de vehiculo no coincide con el tipo de garage");
            return "error";
        }
        v.setTipo(tipo);

        if(largo<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setLargo(largo);
        if(ancho<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setAncho(ancho);

        if (fechaAsignacionGarageAux.isAfter(LocalDate.now())){
            model.addAttribute("error", "No puede ingresar una fecha futura");
            return "error";
        }
        v.setFechaAsignacionGarage(fechaAsignacionGarageAux);

        vehiculoRepository.crear(v);

        Socio s = socioRepository.buscar(socioId);
        socioRepository.agregarVehiculo(socioId,v);

        vehiculoRepository.agregarGarage(v.getId(),g);

        model.addAttribute("vehiculos", vehiculoRepository.obtenerTodos());
        return "vehiculo-list";
    }

    @RequestMapping(value = "/vehiculo/eliminar", method = RequestMethod.POST)
    public String eliminarVehiculo(@RequestParam Integer id, Model model) {

        vehiculoRepository.eliminar(id);
        model.addAttribute("vehiculos", vehiculoRepository.obtenerTodos());
        return "vehiculo-list";
    }

    @RequestMapping(value = "/vehiculo/modificar", method = RequestMethod.POST)
    public String modificarVehiculo(@RequestParam Integer id, Model model) {

        Garage garageSeleccionado = garageRepository.buscarVehiculoId(id);
        List<Garage> garagesLibres = garageRepository.buscarGarageLibres();
        if(garageSeleccionado != null){
            garagesLibres.add(garageSeleccionado);
        }

        Socio socioSeleccionado = socioRepository.buscarSocioId(id);
        List<Socio> sociosLibres = socioRepository.obtenerTodos();

        model.addAttribute("vehiculo", vehiculoRepository.buscar(id));
        model.addAttribute("garageseleccionado", garageSeleccionado );
        model.addAttribute("garages", garagesLibres);

        model.addAttribute("socioseleccionado", socioSeleccionado );
        model.addAttribute("socios", sociosLibres);

        return "vehiculo-update";
    }

    @RequestMapping(value = "/vehiculo/guardar_modificacion", method = RequestMethod.POST)
    public String modificarVehiculo(@RequestParam Integer socioId, @RequestParam String fechaAsignacionGarage, @RequestParam Integer id,@RequestParam Integer garageId, @RequestParam String matricula, @RequestParam String marca, @RequestParam String tipo, @RequestParam Float largo, @RequestParam Float ancho, Model model) {

        LocalDate fechaAsignacionGarageAux = LocalDate.parse(fechaAsignacionGarage, DateTimeFormatter.ISO_DATE);

        Vehiculo v = vehiculoRepository.buscar(id);
        Garage g = garageRepository.buscar(garageId);
        Zona z = zonaRepository.buscarGarageId(g.getId());

        Vehiculo vehiculoEncontrado = vehiculoRepository.buscarVehiculPorMatricula(matricula);

        if(vehiculoEncontrado !=null && vehiculoEncontrado.getId() != id){
            model.addAttribute("error", "Matricula de Vehiculo ya existe!!");
            return "error";
        }
        v.setMatricula(matricula);
        v.setMarca(marca);

        if(!tipo.equals(z.getTipoVehiculos())){
            model.addAttribute("error", "El tipo de vehiculo no coincide con el tipo de garage");
            return "error";
        }
        v.setTipo(tipo);

        if(largo<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setLargo(largo);
        if(ancho<=0){
            model.addAttribute("error", "Numero Invalido. Debe ser mayor a 0");
            return "error";
        }
        v.setAncho(ancho);

        if (fechaAsignacionGarageAux.isAfter(LocalDate.now())){
            model.addAttribute("error", "No puede ingresar una fecha futura");
            return "error";
        }
        v.setFechaAsignacionGarage(fechaAsignacionGarageAux);

        vehiculoRepository.modificar(v);

        Socio s = socioRepository.buscar(socioId);
        socioRepository.agregarVehiculo(socioId,v);

        vehiculoRepository.agregarGarage(id,g);

        model.addAttribute("vehiculos", vehiculoRepository.obtenerTodos());
        return "vehiculo-list";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }
}
