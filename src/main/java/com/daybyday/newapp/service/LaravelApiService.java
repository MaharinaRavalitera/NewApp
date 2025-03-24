package com.daybyday.newapp.service;

import com.daybyday.newapp.dto.LoginRequest;
import com.daybyday.newapp.dto.LoginResponse;
import com.daybyday.newapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class LaravelApiService {

    private final WebClient webClient;
    private boolean demoMode = false;
    
    @Value("${app.demo-mode-fallback:true}")
    private boolean demoModeFallback;
    
    @Autowired
    public LaravelApiService(WebClient.Builder webClientBuilder, 
                           @Value("${laravel.api.base-url}") String apiBaseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(apiBaseUrl)
                .build();
    }
    
    @PostConstruct
    public void init() {
        // Vérifier automatiquement si l'API est disponible au démarrage
        testConnection()
            .doOnSuccess(available -> {
                if (!available && demoModeFallback) {
                    this.demoMode = true;
                    System.out.println("ATTENTION: API Laravel non disponible. Mode démo activé.");
                } else if (available) {
                    System.out.println("API Laravel connectée avec succès.");
                } else {
                    System.out.println("ATTENTION: API Laravel non disponible et le mode démo n'est pas activé.");
                }
            })
            .subscribe();
    }
    
    /**
     * Vérifie si le mode démo est activé
     * @return true si le mode démo est activé
     */
    public boolean isDemoMode() {
        return demoMode;
    }
    
    /**
     * Active ou désactive manuellement le mode démo
     * @param demoMode true pour activer le mode démo
     */
    public void setDemoMode(boolean demoMode) {
        this.demoMode = demoMode;
    }
    
    /**
     * Teste la connexion à l'API Laravel
     * @return true si l'API est accessible, false sinon
     */
    public Mono<Boolean> testConnection() {
        return webClient.get()
                .uri("/")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> true)
                .onErrorReturn(false);
    }
    
    /**
     * Authentifie un utilisateur auprès de l'API Laravel
     * @param loginRequest Les informations d'identification
     * @return Une réponse contenant le token JWT et les informations utilisateur
     */
    public Mono<LoginResponse> authenticate(LoginRequest loginRequest) {
        // Si mode démo, retourner des données simulées
        if (demoMode) {
            System.out.println("Mode démo: Authentification simulée pour " + loginRequest.getEmail());
            
            // Vérifier les identifiants de démo (admin@admin.com/admin123)
            if ("admin@admin.com".equals(loginRequest.getEmail()) && 
                "admin123".equals(loginRequest.getPassword())) {
                
                LoginResponse demoResponse = new LoginResponse();
                demoResponse.setToken("demo_token_" + System.currentTimeMillis());
                demoResponse.setUserId(1L);
                demoResponse.setName("Utilisateur Démo");
                demoResponse.setEmail(loginRequest.getEmail());
                
                return Mono.just(demoResponse);
            } else {
                return Mono.error(new RuntimeException("Identifiants invalides"));
            }
        }
        
        // Sinon, continuer avec l'API normale
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", loginRequest.getEmail());
        requestBody.put("password", loginRequest.getPassword());
        
        System.out.println("Tentative d'authentification avec: " + loginRequest.getEmail());
        
        return webClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("Erreur API Laravel: " + ex.getMessage());
                    System.err.println("Statut: " + ex.getStatusCode());
                    System.err.println("Corps de la réponse: " + ex.getResponseBodyAsString());
                    
                    // Si le mode démo est autorisé en cas d'erreur, basculer en mode démo pour les prochaines requêtes
                    if (demoModeFallback) {
                        demoMode = true;
                        System.out.println("Passage au mode démo suite à une erreur d'API");
                        
                        // Si les identifiants correspondent à ceux de démo, authentifier automatiquement
                        if ("admin@admin.com".equals(loginRequest.getEmail()) && 
                            "admin123".equals(loginRequest.getPassword())) {
                            
                            LoginResponse demoResponse = new LoginResponse();
                            demoResponse.setToken("demo_token_" + System.currentTimeMillis());
                            demoResponse.setUserId(1L);
                            demoResponse.setName("Utilisateur Démo");
                            demoResponse.setEmail(loginRequest.getEmail());
                            
                            return Mono.just(demoResponse);
                        }
                    }
                    
                    return Mono.error(ex);
                });
    }
    
    /**
     * Récupère les informations d'un utilisateur à partir de son token
     * @param token Le token JWT
     * @return Les informations de l'utilisateur
     */
    public Mono<User> getUserInfo(String token) {
        // Si mode démo, retourner des données simulées
        if (demoMode) {
            System.out.println("Mode démo: Informations utilisateur simulées");
            
            User demoUser = new User();
            demoUser.setId(1L);
            demoUser.setName("Utilisateur Démo");
            demoUser.setEmail("admin@admin.com");
            
            return Mono.just(demoUser);
        }
        
        return webClient.get()
                .uri("/auth/me")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("Erreur API Laravel: " + ex.getMessage());
                    System.err.println("Statut: " + ex.getStatusCode());
                    System.err.println("Corps de la réponse: " + ex.getResponseBodyAsString());
                    
                    // Si le mode démo est autorisé en cas d'erreur, basculer en mode démo
                    if (demoModeFallback) {
                        demoMode = true;
                        System.out.println("Passage au mode démo suite à une erreur d'API");
                        
                        User demoUser = new User();
                        demoUser.setId(1L);
                        demoUser.setName("Utilisateur Démo");
                        demoUser.setEmail("admin@admin.com");
                        
                        return Mono.just(demoUser);
                    }
                    
                    return Mono.error(ex);
                });
    }
}
