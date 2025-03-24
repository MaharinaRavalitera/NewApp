package com.daybyday.newapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final WebClient webClient;
    private final LaravelApiService laravelApiService;
    
    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder, 
                           @Value("${laravel.api.base-url}") String apiBaseUrl,
                           LaravelApiService laravelApiService) {
        this.webClient = webClientBuilder
                .baseUrl(apiBaseUrl)
                .build();
        this.laravelApiService = laravelApiService;
    }
    
    /**
     * Récupère les statistiques du tableau de bord
     * @param token Le token JWT de l'utilisateur
     * @return Un Map contenant les statistiques
     */
    public Map<String, Object> getDashboardStatistics(String token) {
        // Si nous sommes en mode démo, renvoyer des données simulées
        if (laravelApiService.isDemoMode()) {
            return getSimulatedDashboardStats();
        }
        
        // Sinon, appeler l'API Laravel pour obtenir les statistiques réelles
        try {
            return webClient.get()
                    .uri("/dashboard/stats")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            // En cas d'erreur, utiliser des données simulées
            return getSimulatedDashboardStats();
        }
    }
    
    /**
     * Récupère les activités récentes pour le tableau de bord
     * @param token Le token JWT de l'utilisateur
     * @return Un Map contenant les activités récentes
     */
    public Map<String, Object> getRecentActivity(String token) {
        // Si nous sommes en mode démo, renvoyer des données simulées
        if (laravelApiService.isDemoMode()) {
            return getSimulatedRecentActivity();
        }
        
        // Sinon, appeler l'API Laravel pour obtenir les activités récentes
        try {
            return webClient.get()
                    .uri("/dashboard/recent")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            // En cas d'erreur, utiliser des données simulées
            return getSimulatedRecentActivity();
        }
    }
    
    /**
     * Génère des données simulées pour les statistiques du tableau de bord
     * @return Un Map contenant les statistiques simulées
     */
    private Map<String, Object> getSimulatedDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClients", 45);
        stats.put("totalProjects", 78);
        stats.put("totalTasks", 156);
        stats.put("totalLeads", 32);
        stats.put("totalInvoices", 93);
        stats.put("totalPayments", 87);
        stats.put("totalRevenue", 125850.75);
        stats.put("outstandingInvoices", 23450.50);
        
        return stats;
    }
    
    /**
     * Génère des données simulées pour les activités récentes
     * @return Un Map contenant les activités récentes simulées
     */
    private Map<String, Object> getSimulatedRecentActivity() {
        // Simuler des données d'activités récentes
        Map<String, Object> recentActivity = new HashMap<>();
        
        // Créer quelques exemples de clients récents
        Map<String, Object> client1 = new HashMap<>();
        client1.put("id", 1);
        client1.put("company_name", "Acme Corporation");
        client1.put("created_at", "2025-03-20T10:15:30");
        
        Map<String, Object> client2 = new HashMap<>();
        client2.put("id", 2);
        client2.put("company_name", "Stark Industries");
        client2.put("created_at", "2025-03-19T14:25:10");
        
        Map<String, Object> client3 = new HashMap<>();
        client3.put("id", 3);
        client3.put("company_name", "Wayne Enterprises");
        client3.put("created_at", "2025-03-18T09:05:45");
        
        // Ajouter d'autres entités simulées (projets, tâches, factures)
        Map<String, Object> project1 = new HashMap<>();
        project1.put("id", 1);
        project1.put("title", "Refonte du site web");
        project1.put("created_at", "2025-03-20T11:30:00");
        
        Map<String, Object> project2 = new HashMap<>();
        project2.put("id", 2);
        project2.put("title", "Application mobile");
        project2.put("created_at", "2025-03-19T10:15:30");
        
        Map<String, Object> task1 = new HashMap<>();
        task1.put("id", 1);
        task1.put("title", "Conception des maquettes");
        task1.put("created_at", "2025-03-20T14:35:00");
        
        Map<String, Object> task2 = new HashMap<>();
        task2.put("id", 2);
        task2.put("title", "Développement backend");
        task2.put("created_at", "2025-03-19T16:42:15");
        
        Map<String, Object> invoice1 = new HashMap<>();
        invoice1.put("id", 1);
        invoice1.put("title", "Facture #INV-2025-001");
        invoice1.put("created_at", "2025-03-20T17:10:00");
        
        // Assembler le tout
        recentActivity.put("recentClients", new Object[]{client1, client2, client3});
        recentActivity.put("recentProjects", new Object[]{project1, project2});
        recentActivity.put("recentTasks", new Object[]{task1, task2});
        recentActivity.put("recentInvoices", new Object[]{invoice1});
        
        return recentActivity;
    }
    
    /**
     * Récupère la liste des clients
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant la liste des clients
     */
    public Mono<Map<String, Object>> getClientsData(String token) {
        return webClient.get()
                .uri("/clients")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère la liste des projets
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant la liste des projets
     */
    public Mono<Map<String, Object>> getProjectsData(String token) {
        return webClient.get()
                .uri("/projects")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère la liste des tâches
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant la liste des tâches
     */
    public Mono<Map<String, Object>> getTasksData(String token) {
        return webClient.get()
                .uri("/tasks")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère la liste des leads
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant la liste des leads
     */
    public Mono<Map<String, Object>> getLeadsData(String token) {
        return webClient.get()
                .uri("/leads")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère la liste des factures
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant la liste des factures
     */
    public Mono<Map<String, Object>> getInvoicesData(String token) {
        return webClient.get()
                .uri("/invoices")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère les statistiques générales
     * @param token Le token JWT de l'utilisateur
     * @return Un Mono contenant les statistiques
     */
    public Mono<Map<String, Object>> getStatisticsData(String token) {
        return webClient.get()
                .uri("/statistics")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Récupère le statut des données factices (nombre d'entrées par type)
     * @param token Le token JWT de l'utilisateur
     * @return Un Map contenant le statut des données (nombre d'entrées par type)
     */
    public Map<String, Object> getDummyDataStatus(String token) {
        try {
            return webClient.get()
                    .uri("/dummy-data/status")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            // En cas d'erreur, retourner des valeurs par défaut
            Map<String, Object> defaultStatus = new HashMap<>();
            defaultStatus.put("clients", 0);
            defaultStatus.put("projects", 0);
            defaultStatus.put("tasks", 0);
            defaultStatus.put("leads", 0);
            defaultStatus.put("invoices", 0);
            defaultStatus.put("comments", 0);
            defaultStatus.put("documents", 0);
            defaultStatus.put("error", e.getMessage());
            return defaultStatus;
        }
    }
    
    /**
     * Génère des données factices selon les paramètres fournis
     * @param token Le token JWT de l'utilisateur
     * @param clients Nombre de clients à générer
     * @param projects Nombre de projets à générer
     * @param tasks Nombre de tâches à générer
     * @param leads Nombre de leads à générer
     * @param invoices Nombre de factures à générer
     * @param comments Nombre de commentaires à générer
     * @param documents Nombre de documents à générer
     * @return Un Map contenant le résultat de la génération
     */
    public Map<String, Object> generateDummyData(String token, int clients, int projects, int tasks, int leads, int invoices, int comments, int documents) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clients", clients);
        requestBody.put("projects", projects);
        requestBody.put("tasks", tasks);
        requestBody.put("leads", leads);
        requestBody.put("invoices", invoices);
        requestBody.put("comments", comments);
        requestBody.put("documents", documents);
        
        try {
            return webClient.post()
                    .uri("/dummy-data/generate")
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            // En cas d'erreur, retourner un message d'erreur
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
    
    /**
     * Supprime toutes les données factices générées
     * @param token Le token JWT de l'utilisateur
     * @return Un Map contenant le résultat de l'opération
     */
    public Map<String, Object> cleanDummyData(String token) {
        try {
            return webClient.post()
                    .uri("/dummy-data/clean")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            // En cas d'erreur, retourner un message d'erreur
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
}
