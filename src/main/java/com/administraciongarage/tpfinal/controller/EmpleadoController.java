package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.Empleado;
import com.administraciongarage.tpfinal.entity.Usuario;
import com.administraciongarage.tpfinal.entity.ZonaAsignada;
import com.administraciongarage.tpfinal.repository.EmpleadoRepository;
import com.administraciongarage.tpfinal.repository.UsuarioRepository;
import com.administraciongarage.tpfinal.repository.ZonaAsignadaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class EmpleadoController {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ZonaAsignadaRepository zonaAsignadaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping(value = "/menu-empleado", method = RequestMethod.POST)
    public String menuEmpleado(@RequestParam Integer id,Model model) {

        model.addAttribute("id",id);
        return "menu-empleado";
    }

    @RequestMapping(value = "/empleado-datos", method = RequestMethod.POST)
    public String verDatos(@RequestParam Integer id,Model model) {

        model.addAttribute("empleado", empleadoRepository.buscar(id));
        model.addAttribute("zonasasignadas", zonaAsignadaRepository.obtenerZonasAsignadas(id));
        return "empleado-datos";
    }

    @RequestMapping(value = "/empleados", method = RequestMethod.GET)
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoRepository.obtenerTodos());
        return "empleado-list";
    }

    @RequestMapping(value = "/empleado/crear", method = RequestMethod.GET)
    public String crearEmpleado() {
        return "empleado-add";
    }

    @RequestMapping(value = "/empleado/crear", method = RequestMethod.POST)
    public String crearEmpleado(@RequestParam String especialidad, @RequestParam String codigo, @RequestParam String nombre, @RequestParam String direccion, @RequestParam String telefono, @RequestParam Integer dni,@RequestParam String user, @RequestParam String pwd, Model model) {

        Empleado v = new Empleado();
        v.setEspecialidad(especialidad);

        if(empleadoRepository.buscarEmpleadoPorCodigo(codigo)!=null){
            model.addAttribute("error", "Codigo de empleado ya existe!!");
            return "error";
        }
        v.setCodigo(codigo);
        v.setNombre(nombre);
        v.setDireccion(direccion);
        v.setTelefono(telefono);

        if(dni <=0){
            model.addAttribute("error", "DNI Invalido. Debe ser mayor a 0");
            return "error";
        }
        if(usuarioRepository.buscarUsuarioPorDNI(dni) != null){
            model.addAttribute("error", "DNI de empleado ya existe!!");
            return "error";
        }
        v.setDni(dni);

        if(usuarioRepository.buscarUsuarioPorUser(user) != null){
            model.addAttribute("error", "Identificador de Usuario no disponible!!");
            return "error";
        }
        v.setUser(user);

        v.setPwd(pwd);
        v.setZonaEmpleado(null);
        empleadoRepository.crear(v);

        model.addAttribute("empleados", empleadoRepository.obtenerTodos());
        return "empleado-list";
    }

    @RequestMapping(value = "/empleado/eliminar", method = RequestMethod.POST)
    public String eliminarEmpleado(@RequestParam Integer id, Model model) {

        List<ZonaAsignada> zonas = zonaAsignadaRepository.obtenerZonasAsignadas(id);
        if(zonas.size()>0 ){
            model.addAttribute("error", "Empleado no se puede eliminar. Tiene zonas asignadas");
            return "error";
        }

        empleadoRepository.eliminar(id);
        model.addAttribute("empleados", empleadoRepository.obtenerTodos());
        return "empleado-list";
    }

    @RequestMapping(value = "/empleado/modificar", method = RequestMethod.POST)
    public String modificarEmpleado(@RequestParam Integer id, Model model) {

        model.addAttribute("empleado", empleadoRepository.buscar(id));
        return "empleado-update";
    }

    @RequestMapping(value = "/empleado/guardar_modificacion", method = RequestMethod.POST)
    public String modificarEmpleado(@RequestParam Integer id, @RequestParam String especialidad, @RequestParam String codigo, @RequestParam String nombre, @RequestParam String direccion, @RequestParam String telefono, @RequestParam Integer dni,@RequestParam String user, @RequestParam String pwd, Model model) {

        Empleado v = empleadoRepository.buscar(id);
        v.setEspecialidad(especialidad);

        Empleado empleadoEncontradoCodigo = empleadoRepository.buscarEmpleadoPorCodigo(codigo);
        if(empleadoEncontradoCodigo !=null && empleadoEncontradoCodigo.getId() != id){
            model.addAttribute("error", "Codigo de empleado ya existe!!");
            return "error";
        }
        v.setCodigo(codigo);

        v.setNombre(nombre);
        v.setDireccion(direccion);
        v.setTelefono(telefono);

        if(dni <=0){
            model.addAttribute("error", "DNI Invalido. Debe ser mayor a 0");
            return "error";
        }
        Usuario usuarioEncontradoDni = usuarioRepository.buscarUsuarioPorDNI(dni);
        if(usuarioEncontradoDni !=null && usuarioEncontradoDni.getId() != id){
            model.addAttribute("error", "DNI de empleado ya existe!!");
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
        v.setZonaEmpleado(null);
        empleadoRepository.modificar(v);

        model.addAttribute("empleados", empleadoRepository.obtenerTodos());
        return "empleado-list";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }
}
