package com.administraciongarage.tpfinal.controller;

import com.administraciongarage.tpfinal.entity.Usuario;
import com.administraciongarage.tpfinal.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String crearSocio(@RequestParam String user, @RequestParam String pwd, Model model) {

        String salida;

        Usuario u = usuarioRepository.login(user,pwd);

        if(u == null){
            model.addAttribute("error", "Login Invalido");
            return "error";
        }

        model.addAttribute("id",u.getId());

        return u.getVista();
    }

    @RequestMapping(value = "/menu-admin", method = RequestMethod.GET)
    public String menuAdmin(Model model) {

        return "menu-admin";
    }

    @ExceptionHandler
    public String handle(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", e);
        return "error";
    }

}
