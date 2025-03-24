package com.daybyday.newapp.controller;

import com.daybyday.newapp.service.DashboardService;
import com.daybyday.newapp.service.LaravelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final LaravelApiService laravelApiService;

    @Autowired
    public DashboardController(DashboardService dashboardService, LaravelApiService laravelApiService) {
        this.dashboardService = dashboardService;
        this.laravelApiService = laravelApiService;
    }

    @GetMapping("")
    public String dashboard(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        System.out.println("DashboardController - Token récupéré dans la session: " + (token != null ? "présent" : "absent"));
        
        if (token == null) {
            System.out.println("DashboardController - Aucun token trouvé, redirection vers la page d'accueil");
            return "redirect:/";  
        }

        // Récupérer les statistiques pour le tableau de bord
        Map<String, Object> dashboardStats = dashboardService.getDashboardStatistics(token);
        
        // Récupérer les activités récentes
        Map<String, Object> recentActivity = dashboardService.getRecentActivity(token);
        
        // Ajouter les informations au modèle
        model.addAttribute("stats", dashboardStats);
        model.addAttribute("recentActivity", recentActivity);
        model.addAttribute("demoMode", laravelApiService.isDemoMode());
        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("activeMenu", "dashboard");
        
        return "dashboard/index";
    }
    
    @GetMapping("/clients")
    public String clients(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }

        try {
            // Récupérer la liste des clients
            Map<String, Object> clientsData = dashboardService.getClientsData(token).block();
            model.addAttribute("clients", clientsData);
            model.addAttribute("demoMode", laravelApiService.isDemoMode());
        } catch (Exception e) {
            // En cas d'erreur, informer l'utilisateur
            model.addAttribute("error", "Impossible de récupérer la liste des clients: " + e.getMessage());
            model.addAttribute("demoMode", true);
        }
        
        model.addAttribute("pageTitle", "Clients");
        model.addAttribute("activeMenu", "clients");
        
        return "dashboard/clients";
    }
    
    @GetMapping("/projects")
    public String projects(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }

        try {
            // Récupérer la liste des projets
            Map<String, Object> projectsData = dashboardService.getProjectsData(token).block();
            model.addAttribute("projects", projectsData);
            model.addAttribute("demoMode", laravelApiService.isDemoMode());
        } catch (Exception e) {
            // En cas d'erreur, informer l'utilisateur
            model.addAttribute("error", "Impossible de récupérer la liste des projets: " + e.getMessage());
            model.addAttribute("demoMode", true);
        }
        
        model.addAttribute("pageTitle", "Projets");
        model.addAttribute("activeMenu", "projects");
        
        return "dashboard/projects";
    }
    
    @GetMapping("/tasks")
    public String tasks(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }

        try {
            // Récupérer la liste des tâches
            Map<String, Object> tasksData = dashboardService.getTasksData(token).block();
            model.addAttribute("tasks", tasksData);
            model.addAttribute("demoMode", laravelApiService.isDemoMode());
        } catch (Exception e) {
            // En cas d'erreur, informer l'utilisateur
            model.addAttribute("error", "Impossible de récupérer la liste des tâches: " + e.getMessage());
            model.addAttribute("demoMode", true);
        }
        
        model.addAttribute("pageTitle", "Tâches");
        model.addAttribute("activeMenu", "tasks");
        
        return "dashboard/tasks";
    }
    
    @GetMapping("/leads")
    public String leads(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }

        try {
            // Récupérer la liste des leads
            Map<String, Object> leadsData = dashboardService.getLeadsData(token).block();
            model.addAttribute("leads", leadsData);
            model.addAttribute("demoMode", laravelApiService.isDemoMode());
        } catch (Exception e) {
            // En cas d'erreur, informer l'utilisateur
            model.addAttribute("error", "Impossible de récupérer la liste des leads: " + e.getMessage());
            model.addAttribute("demoMode", true);
        }
        
        model.addAttribute("pageTitle", "Leads");
        model.addAttribute("activeMenu", "leads");
        
        return "dashboard/leads";
    }
    
    @GetMapping("/invoices")
    public String invoices(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }

        try {
            // Récupérer la liste des factures
            Map<String, Object> invoicesData = dashboardService.getInvoicesData(token).block();
            model.addAttribute("invoices", invoicesData);
            model.addAttribute("demoMode", laravelApiService.isDemoMode());
        } catch (Exception e) {
            // En cas d'erreur, informer l'utilisateur
            model.addAttribute("error", "Impossible de récupérer la liste des factures: " + e.getMessage());
            model.addAttribute("demoMode", true);
        }
        
        model.addAttribute("pageTitle", "Factures");
        model.addAttribute("activeMenu", "invoices");
        
        return "dashboard/invoices";
    }
    
    @GetMapping("/dummy-data")
    public String dummyDataGenerator(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }
        
        try {
            // Récupérer le statut actuel des données
            Map<String, Object> dummyDataStatus = dashboardService.getDummyDataStatus(token);
            model.addAttribute("dataStatus", dummyDataStatus);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer le statut des données: " + e.getMessage());
        }
        
        model.addAttribute("demoMode", laravelApiService.isDemoMode());
        model.addAttribute("pageTitle", "Générateur de données factices");
        model.addAttribute("activeMenu", "dummy-data");
        
        return "dashboard/dummy-data";
    }
    
    @PostMapping("/generate-dummy-data")
    public String generateDummyData(
            @RequestParam("clients") int clients,
            @RequestParam("projects") int projects,
            @RequestParam("tasks") int tasks,
            @RequestParam("leads") int leads,
            @RequestParam("invoices") int invoices,
            @RequestParam("comments") int comments,
            @RequestParam("documents") int documents,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
            
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }
        
        try {
            // Générer les données factices
            Map<String, Object> result = dashboardService.generateDummyData(token, clients, projects, tasks, leads, invoices, comments, documents);
            redirectAttributes.addFlashAttribute("success", "Données factices générées avec succès");
            redirectAttributes.addFlashAttribute("generationResult", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la génération des données factices: " + e.getMessage());
        }
        
        return "redirect:/dashboard/dummy-data";
    }
    
    @PostMapping("/clean-dummy-data")
    public String cleanDummyData(HttpSession session, RedirectAttributes redirectAttributes) {
        // Vérifier si l'utilisateur est connecté
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/";  
        }
        
        try {
            // Nettoyer les données factices
            dashboardService.cleanDummyData(token);
            redirectAttributes.addFlashAttribute("success", "Données factices supprimées avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression des données factices: " + e.getMessage());
        }
        
        return "redirect:/dashboard/dummy-data";
    }
}
