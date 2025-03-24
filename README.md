# DayByDay NewApp

Application Spring Boot qui s'intègre avec l'application Laravel DayByDay CRM.

## Description

Cette application Spring Boot sert d'interface complémentaire au CRM DayByDay développé en Laravel. Elle permet de se connecter à l'API Laravel pour accéder aux données du CRM et offre une interface utilisateur alternative.

## Fonctionnalités

- Authentification via l'API Laravel
- Mode démo automatique si l'API n'est pas disponible
- Tableau de bord avec statistiques
- Gestion des clients, projets, tâches, leads et factures
- Générateur de données factices pour le CRM

## Prérequis

- Java 11 ou supérieur
- Maven
- Application Laravel DayByDay CRM accessible (ou mode démo activé)

## Installation

1. Cloner ce dépôt
2. Configurer l'URL de l'API Laravel dans `src/main/resources/application.properties`
3. Exécuter l'application avec Maven :
   ```
   mvn spring-boot:run
   ```

## Configuration

Les principales options de configuration se trouvent dans le fichier `application.properties` :

```properties
# Server configuration
server.port=8002

# Laravel API configuration
laravel.api.base-url=http://localhost:8000/api
laravel.api.version=v1

# Mode démo (s'active automatiquement si l'API n'est pas disponible)
app.demo-mode-fallback=true
```

## Utilisation

1. Accéder à l'application via http://localhost:8002
2. Se connecter avec les identifiants du CRM Laravel
3. En mode démo, utiliser admin@admin.com / admin123

## Développement

Ce projet a été développé dans le cadre d'une évaluation pour démontrer l'intégration entre une application Spring Boot et une API Laravel.
