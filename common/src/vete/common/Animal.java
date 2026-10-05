package vete.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

// pour A1
public interface Animal extends Remote{
    String getNom() throws RemoteException;

    String getNomMaitre() throws RemoteException;

    String getRace() throws RemoteException;

    //A2
    Espece getEspece() throws RemoteException;


    //A3
    DossierSuivi getDossier() throws RemoteException;

}
