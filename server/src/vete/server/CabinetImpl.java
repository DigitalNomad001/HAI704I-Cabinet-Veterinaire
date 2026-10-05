package vete.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import vete.common.Animal;
import vete.common.Cabinet;
import vete.common.PatientInconnuException;
import vete.common.PatientExistantException;
import vete.common.Espece;
import vete.common.CabinetObserver;
public class CabinetImpl extends UnicastRemoteObject implements Cabinet {

    //pour A6
    // Liste thread-safe : on peut la parcourir pendant qu'un autre client s'abonne
    private final List<CabinetObserver> observateurs = new CopyOnWriteArrayList<>();

    private static final int[] SEUILS = {100, 500, 1000};

    // J'ai décidé que la liste de patients soit un HashMaps pour qu'on puisse chercher un patient par nom
    private final Map<String, AnimalImpl> patients = new HashMap<>();

    public CabinetImpl() throws RemoteException {
        super();
    }

    // Méthode LOCALE (hors interface) : utilisée par le serveur au démarrage
    public synchronized void ajouter(AnimalImpl animal) throws RemoteException {
        patients.put(animal.getNom(), animal);
    }

    @Override
    public int getNombrePatients() throws RemoteException{
        return patients.size();
    }

    @Override
    public synchronized List<Animal> getPatients() throws RemoteException {
        return new ArrayList<>(patients.values());
    }

    @Override
    public synchronized Animal rechercher(String nom) throws RemoteException, PatientInconnuException {
        AnimalImpl a = patients.get(nom);
        if (a == null) {
            throw new PatientInconnuException(nom);
        }
        return a;
    }
/*
    @Override
    public synchronized Animal ajouterPatient(String nom, String nomMaitre, String race, Espece espece)
            throws RemoteException, PatientExistantException {

        if (patients.containsKey(nom)) {
            throw new PatientExistantException(nom);
        }

        // L'animal est instancié ICI, dans la JVM du serveur :
        // c'est la seule façon d'obtenir un objet distant hébergé par le serveur
        AnimalImpl nouvel = new AnimalImpl(nom, nomMaitre, race, espece);
        patients.put(nom, nouvel);

        System.out.println("[serveur] patient ajouté : " + nom + " (" + patients.size() + " patients)");
        return nouvel;   // RMI renvoie un STUB au client
    }


 */

    @Override
    public Animal ajouterPatient(String nom, String nomMaitre, String race, Espece espece)
            throws RemoteException, PatientExistantException {
        AnimalImpl nouvel;
        int avant, apres;
        synchronized (this) {
            if (patients.containsKey(nom)) {
                throw new PatientExistantException(nom);
            }
            nouvel = new AnimalImpl(nom, nomMaitre, race, espece);
            avant = patients.size();
            patients.put(nom, nouvel);
            apres = patients.size();
        }
        verifierSeuils(avant, apres);   // notifications HORS du verrou
        return nouvel;
    }

    @Override
    public void supprimerPatient(String nom)
            throws RemoteException, PatientInconnuException {
        int avant,apres;
        avant=patients.size();
        synchronized(this) {
            AnimalImpl retire = patients.remove(nom);
            if (retire == null) {
                throw new PatientInconnuException(nom);
            }

            // On retire l'objet du runtime RMI : il n'accepte plus d'appels distants
            UnicastRemoteObject.unexportObject(retire, true);
            apres = patients.size();
        }
        System.out.println("[serveur] patient supprimé : " + nom + " (" + patients.size() + " patients)");
        verifierSeuils(avant, apres);

    }

    @Override
    public void abonner(CabinetObserver observateur) throws RemoteException {
        if (!observateurs.contains(observateur)) {
            observateurs.add(observateur);
        }
        System.out.println("[serveur] abonnement (" + observateurs.size() + " abonné(s))");
    }

    @Override
    public void desabonner(CabinetObserver observateur) throws RemoteException {
        observateurs.remove(observateur);
        System.out.println("[serveur] désabonnement (" + observateurs.size() + " abonné(s))");
    }

    // Appelée après chaque ajout / suppression
    private void verifierSeuils(int avant, int apres) {
        for (int seuil : SEUILS) {
            if (avant < seuil && apres >= seuil) {
                notifier(seuil, apres, true);     // franchi à la hausse
            } else if (avant >= seuil && apres < seuil) {
                notifier(seuil, apres, false);    // franchi à la baisse
            }
        }
    }

    private void notifier(int seuil, int nombre, boolean hausse) {
        for (CabinetObserver obs : observateurs) {
            try {
                obs.alerteSeuil(seuil, nombre, hausse);   // appel DISTANT vers un client
            } catch (RemoteException e) {
                // Robustesse : ce client a été fermé entre-temps -> on le retire et on continue
                observateurs.remove(obs);
                System.out.println("[serveur] abonné injoignable retiré : " + e.getClass().getSimpleName());
            }
        }
    }



}