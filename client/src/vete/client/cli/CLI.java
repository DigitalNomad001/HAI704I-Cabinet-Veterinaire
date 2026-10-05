package vete.client.cli;

import java.util.List;
import java.util.Scanner;

import vete.client.src.CabinetService;
import vete.client.src.ClientException;

// INTERFACE CONSOLE : menu, saisies, affichage. Aucun import java.rmi ici.
public class CLI {

    private final CabinetService service;
    private final Scanner sc = new Scanner(System.in);

    public CLI(CabinetService service) {
        this.service = service;
    }

    public void lancer() {
        System.out.println("=== Cabinet vétérinaire ===");
        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            String choix = lire("Votre choix");
            if (choix == null) break;   // fin de l'entrée (Ctrl+D)
            try {
                switch (choix) {
                    case "1" -> lister();
                    case "2" -> rechercher();
                    case "3" -> enregistrer();
                    case "4" -> modifierEtat();
                    case "5" -> ajouterObservation();
                    case "6" -> abonner();
                    case "7" -> desabonner();
                    case "0" -> continuer = false;
                    default  -> System.out.println("Choix invalide : tapez un chiffre du menu.");
                }
            } catch (ClientException e) {
                System.out.println("Erreur : " + e.getMessage());   // message lisible, le client continue
            }
        }
        service.fermer();
        System.out.println("Au revoir.");
    }

    private void afficherMenu() {
        System.out.println();
        System.out.println("1. Lister les patients");
        System.out.println("2. Rechercher un patient (fiche + dossier)");
        System.out.println("3. Enregistrer un nouveau patient");
        System.out.println("4. Modifier l'état de santé");
        System.out.println("5. Ajouter une observation au dossier");
        System.out.println("6. S'abonner aux alertes" + (service.estAbonne() ? " (déjà abonné)" : ""));
        System.out.println("7. Se désabonner des alertes");
        System.out.println("0. Quitter");
    }

    // ---------- Actions ----------

    private void lister() throws ClientException {
        List<String> patients = service.listerPatients();
        System.out.println(patients.size() + " patient(s) :");
        for (String p : patients) {
            System.out.println("  - " + p);
        }
    }

    private void rechercher() throws ClientException {
        String nom = lireObligatoire("Nom du patient");
        System.out.print(service.consulter(nom));
    }

    private void enregistrer() throws ClientException {
        String nom = lireObligatoire("Nom");
        String maitre = lireObligatoire("Nom du maître");
        String race = lireObligatoire("Race");
        String espece = lireObligatoire("Espèce (ex. Chien, Chat)");
        double esperance = lireNombre("Espérance de vie moyenne (années)");
        service.enregistrer(nom, maitre, race, espece, esperance);
        System.out.println("Patient enregistré. Nombre de patients : " + service.nombrePatients());
    }

    private void modifierEtat() throws ClientException {
        String nom = lireObligatoire("Nom du patient");
        String etat = lireObligatoire("Nouvel état de santé");
        service.modifierEtat(nom, etat);
        System.out.println("État mis à jour.");
    }

    private void ajouterObservation() throws ClientException {
        String nom = lireObligatoire("Nom du patient");
        String obs = lireObligatoire("Observation");
        service.ajouterObservation(nom, obs);
        System.out.println("Observation ajoutée.");
    }

    private void abonner() throws ClientException {
        service.abonner(this::afficherAlerte);
        System.out.println("Abonné aux alertes (seuils 100, 500, 1000).");
    }

    private void desabonner() throws ClientException {
        service.desabonner();
        System.out.println("Désabonné des alertes.");
    }

    // ---------- Affichage des alertes ----------

    // Appelée par un thread RMI, éventuellement pendant une saisie
    private void afficherAlerte(String message) {
        synchronized (System.out) {
            System.out.println();
            System.out.println("  >>> ALERTE : " + message);
            System.out.print("  (poursuivez votre saisie) > ");
            System.out.flush();
        }
    }

    // ---------- Saisies ----------

    private String lire(String invite) {
        System.out.print(invite + " > ");
        if (!sc.hasNextLine()) return null;
        return sc.nextLine().trim();
    }

    // Redemande tant que la saisie est vide
    private String lireObligatoire(String invite) {
        while (true) {
            String s = lire(invite);
            if (s == null) return "";
            if (!s.isEmpty()) return s;
            System.out.println("Ce champ est obligatoire.");
        }
    }

    // Redemande tant que ce n'est pas un nombre strictement positif
    private double lireNombre(String invite) {
        while (true) {
            String s = lireObligatoire(invite);
            try {
                double d = Double.parseDouble(s.replace(',', '.'));
                if (d > 0) return d;
                System.out.println("Le nombre doit être strictement positif.");
            } catch (NumberFormatException e) {
                System.out.println("\"" + s + "\" n'est pas un nombre valide (ex. 12 ou 12.5).");
            }
        }
    }
}