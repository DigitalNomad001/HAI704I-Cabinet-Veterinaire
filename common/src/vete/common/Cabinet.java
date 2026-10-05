package vete.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Cabinet extends Remote {

    // Les animaux sont des objets distants : le client recevra une liste de STUBS
    List<Animal> getPatients() throws RemoteException;

    // Recherche par nom ; lève PatientInconnuException si aucun patient ne porte ce nom
    Animal rechercher(String nom) throws RemoteException, PatientInconnuException;


    //A5
    Animal ajouterPatient(String nom, String nomMaitre, String race, Espece espece)
            throws RemoteException, PatientExistantException;

    // Permet de franchir les seuils a la baisse pour A6.
    void supprimerPatient(String nom) throws RemoteException, PatientInconnuException;

    // A6 — abonnement / desabonnement aux alertes de seuil.
    void abonner(CabinetObserver observateur) throws RemoteException;

    void desabonner(CabinetObserver observateur) throws RemoteException;
    int getNombrePatients() throws  RemoteException;
}