package vete.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CabinetObserver extends Remote {

    // seuil : 100, 500 ou 1000 ; hausse = true si franchi à la hausse, false à la baisse
    void alerteSeuil(int seuil, int nombrePatients, boolean hausse) throws RemoteException;
}