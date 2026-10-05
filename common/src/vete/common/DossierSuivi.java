package vete.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// pour A3, j'ai choisi que :
// le dossier de suivi est un objet distant (il reste sur le serveur, on l’appelle à distance)

public interface DossierSuivi extends Remote {

    String getEtatSante() throws RemoteException;

    void setEtatSante(String etat) throws RemoteException;

    void ajouterObservation(String note) throws RemoteException;

    /** Renvoie une COPIE de l'historique (une List est transmise par valeur). */
    List<String> getHistorique() throws RemoteException;
}
