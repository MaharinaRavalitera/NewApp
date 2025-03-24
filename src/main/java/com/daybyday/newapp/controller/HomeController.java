package com.daybyday.newapp.controller;

import com.daybyday.newapp.dto.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        if (session.getAttribute("token") != null) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("loginRequest", new LoginRequest());
            return "home";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
}
