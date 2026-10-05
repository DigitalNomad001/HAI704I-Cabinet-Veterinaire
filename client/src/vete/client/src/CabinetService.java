package vete.client.src;

import java.rmi.ConnectException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import vete.common.*;

// LOGIQUE CLIENT : toutes les opérations sur les interfaces distantes.
// Chaque échec est traduit en ClientException avec un message compréhensible.
public class CabinetService {

    private final Cabinet cabinet;
    private ObservateurImpl observateur;   // null si non abonné

    public CabinetService(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    // ---------- Patients ----------

    public List<String> listerPatients() throws ClientException {
        try {
            List<String> resultat = new ArrayList<>();
            for (Animal a : cabinet.getPatients()) {
                resultat.add(a.getNom() + " (" + a.getEspece().getNom_espece() + ", " + a.getRace()
                        + ", maître : " + a.getNomMaitre() + ")");
            }
            return resultat;
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public String consulter(String nom) throws ClientException {
        try {
            Animal a = cabinet.rechercher(nom);
            Espece e = a.getEspece();
            DossierSuivi d = a.getDossier();
            StringBuilder sb = new StringBuilder();
            sb.append("  Nom     : ").append(a.getNom()).append("\n");
            sb.append("  Maître  : ").append(a.getNomMaitre()).append("\n");
            sb.append("  Race    : ").append(a.getRace()).append("\n");
            sb.append("  Espèce  : ").append(e.getNom_espece())
                    .append(" (espérance de vie ").append(e.getEsperanceVieMoyenne()).append(" ans)\n");
            sb.append("  État    : ").append(d.getEtatSante()).append("\n");
            sb.append("  Historique :\n");
            for (String ligne : d.getHistorique()) {
                sb.append("    ").append(ligne).append("\n");
            }
            return sb.toString();
        } catch (PatientInconnuException e) {
            throw new ClientException(e.getMessage());
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public int nombrePatients() throws ClientException {
        try {
            return cabinet.getNombrePatients();
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public void enregistrer(String nom, String maitre, String race,
                            String nomEspece, double esperanceVie) throws ClientException {
        try {
            cabinet.ajouterPatient(nom, maitre, race, new Espece(nomEspece, esperanceVie));
        } catch (PatientExistantException e) {
            throw new ClientException(e.getMessage());
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    // ---------- Dossier ----------

    public void modifierEtat(String nom, String etat) throws ClientException {
        try {
            cabinet.rechercher(nom).getDossier().setEtatSante(etat);
        } catch (PatientInconnuException e) {
            throw new ClientException(e.getMessage());
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public void ajouterObservation(String nom, String observation) throws ClientException {
        try {
            cabinet.rechercher(nom).getDossier().ajouterObservation(observation);
        } catch (PatientInconnuException e) {
            throw new ClientException(e.getMessage());
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    // ---------- Alertes ----------

    public boolean estAbonne() {
        return observateur != null;
    }

    public void abonner(Consumer<String> affichage) throws ClientException {
        if (observateur != null) {
            throw new ClientException("Vous êtes déjà abonné aux alertes.");
        }
        try {
            observateur = new ObservateurImpl(affichage);
            cabinet.abonner(observateur);
        } catch (RemoteException e) {
            liberer();   // l'abonnement a échoué : on ne garde pas un objet exporté inutile
            throw traduire(e);
        }
    }

    public void desabonner() throws ClientException {
        if (observateur == null) {
            throw new ClientException("Vous n'êtes pas abonné aux alertes.");
        }
        try {
            cabinet.desabonner(observateur);
        } catch (RemoteException e) {
            throw traduire(e);
        } finally {
            liberer();
        }
    }

    // Arrêt propre : désabonnement (si possible) puis dés-export de l'observateur.
    // Sans unexportObject, le thread RMI d'écoute garderait la JVM du client en vie.
    public void fermer() {
        if (observateur != null) {
            try {
                cabinet.desabonner(observateur);
            } catch (RemoteException e) {
                // serveur déjà arrêté : on libère quand même
            }
            liberer();
        }
    }

    private void liberer() {
        if (observateur != null) {
            try {
                UnicastRemoteObject.unexportObject(observateur, true);
            } catch (NoSuchObjectException e) {
                // déjà dés-exporté
            }
            observateur = null;
        }
    }

    // ---------- Traduction des erreurs distantes ----------

    private ClientException traduire(RemoteException e) {
        if (e instanceof ConnectException) {
            return new ClientException("Serveur injoignable (arrêté ou réseau indisponible).");
        }
        if (e instanceof NoSuchObjectException) {
            return new ClientException("Cet objet n'existe plus sur le serveur (serveur redémarré ?).");
        }
        return new ClientException("Erreur de communication avec le serveur : "
                + e.getClass().getSimpleName());
    }
}