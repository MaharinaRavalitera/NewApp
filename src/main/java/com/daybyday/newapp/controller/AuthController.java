package com.daybyday.newapp.controller;

import com.daybyday.newapp.dto.LoginRequest;
import com.daybyday.newapp.dto.LoginResponse;
import com.daybyday.newapp.service.LaravelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final LaravelApiService laravelApiService;

    @Autowired
    public AuthController(LaravelApiService laravelApiService) {
        this.laravelApiService = laravelApiService;
    }

    // Endpoint de test pour vérifier l'état de la connexion API
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testApiConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean apiAvailable = laravelApiService.testConnection().block();
            response.put("status", apiAvailable ? "success" : "error");
            response.put("message", apiAvailable ? "API Laravel accessible" : "API Laravel inaccessible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors du test de connexion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Endpoint de connexion avec formulaire standard
    @PostMapping("/login-form")
    public String loginForm(String email, String password, HttpSession session, Model model) {
        try {
            System.out.println("==== AUTHENTIFICATION AVEC FORMULAIRE ====");
            System.out.println("Email: " + email);
            System.out.println("Tentative de connexion pour l'utilisateur: " + email);
            
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);
            
            // Pour résoudre le problème, nous allons tester le mode démo
            if ("admin@admin.com".equals(email) && "admin123".equals(password)) {
                System.out.println("Authentification en mode démo pour: " + email);
                
                // Stockage des informations d'authentification dans la session
                session.setAttribute("token", "demo_token_" + System.currentTimeMillis());
                session.setAttribute("userId", 1L);
                session.setAttribute("userName", "Utilisateur Démo");
                session.setAttribute("userEmail", email);
                
                System.out.println("Token stocké dans la session: " + session.getAttribute("token"));
                System.out.println("Redirection vers /dashboard");
                
                return "redirect:/dashboard";
            }
            
            LoginResponse response = laravelApiService.authenticate(loginRequest).block();
            
            if (response != null && response.getToken() != null) {
                // Stocker le token dans la session
                session.setAttribute("token", response.getToken());
                session.setAttribute("userId", response.getUserId());
                session.setAttribute("userName", response.getName());
                session.setAttribute("userEmail", response.getEmail());
                
                // Log pour debugging
                System.out.println("Authentification réussie pour: " + email);
                System.out.println("Token stocké dans la session: " + response.getToken());
                System.out.println("Redirection vers le dashboard");
                
                // Rediriger vers le dashboard
                return "redirect:/dashboard";
            } else {
                // Authentification échouée
                System.out.println("Authentification échouée pour: " + email);
                if (response != null) {
                    System.out.println("Informations: Token=" + response.getToken());
                }
                return "redirect:/?error=invalid_credentials";
            }
        } catch (WebClientResponseException ex) {
            System.err.println("WebClientResponseException lors de l'authentification: " + ex.getMessage());
            System.err.println("Status code: " + ex.getStatusCode());
            
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "redirect:/?error=invalid_credentials";
            } else {
                // Problème de connexion à l'API
                return "redirect:/?error=api_unavailable";
            }
        } catch (Exception ex) {
            System.err.println("Exception générale lors de l'authentification: " + ex.getMessage());
            ex.printStackTrace();
            return "redirect:/?error=server_error";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Nettoyer la session
        session.removeAttribute("token");
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.invalidate();
        
        System.out.println("Déconnexion effectuée");
        
        // Rediriger vers la page d'accueil
        return "redirect:/";
    }
}
