package vete.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vete.common.DossierSuivi;

public class DossierSuiviImpl extends UnicastRemoteObject implements DossierSuivi {

    private String etatSante;
    private final List<String> historique = new ArrayList<>();

    public DossierSuiviImpl(String etatInitial) throws RemoteException {
        super();
        this.etatSante = etatInitial;
        historique.add("[" + LocalDateTime.now() + "] :"+" Ouverture du dossier (etat : " + etatInitial + ")");
    }

    @Override
    public synchronized String getEtatSante() throws RemoteException {
        return etatSante;
    }

    @Override
    public synchronized void setEtatSante(String etat) throws RemoteException {
        historique.add("[" + LocalDateTime.now() + "] ："+" Etat : " + etatSante + " -> " + etat);
        this.etatSante = etat;
    }

    @Override
    public synchronized void ajouterObservation(String observation) throws RemoteException {
        historique.add( "[" + LocalDateTime.now() + "] :"+observation);
    }

    @Override
    public synchronized List<String> getHistorique() throws RemoteException {
        return new ArrayList<>(historique);
    }

}
