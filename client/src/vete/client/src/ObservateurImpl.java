package vete.client.src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import vete.common.CabinetObserver;
import java.util.function.Consumer;

public class ObservateurImpl extends UnicastRemoteObject implements CabinetObserver {
    private final transient Consumer<String> affichage;   // fourni par l'interface console

    public ObservateurImpl(Consumer<String> affichage) throws RemoteException {
        super();   // export : le CLIENT devient hébergeur d'un objet distant
        this.affichage = affichage;
    }

    @Override
    public void alerteSeuil(int seuil, int nombrePatients, boolean hausse) throws RemoteException {
        // Exécuté dans la JVM du CLIENT, appelé à distance par le serveur
        affichage.accept("\n>>> ALERTE : seuil de " + seuil + " patients franchi à la "
                + (hausse ? "HAUSSE" : "BAISSE") + " (" + nombrePatients + " patients)");
    }
}