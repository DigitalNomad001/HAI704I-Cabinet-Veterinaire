package vete.server;

import vete.common.Animal;
import vete.common.Espece;
import vete.common.DossierSuivi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AnimalImpl extends UnicastRemoteObject implements Animal {

    private String nom;
    private  String nomMaitre;
    private String race;
    //private String espece;
    private Espece espece;
    private DossierSuivi dossier;


    public AnimalImpl(String nom, String nomMaitre, String race,Espece espece) throws RemoteException {
        super();
        this.nom = nom;
        this.nomMaitre = nomMaitre;
        this.race = race;
        this.espece = espece;
        this.dossier=new DossierSuiviImpl("Initial");

    }
    @Override
    public String getNom() throws RemoteException{
        return nom;
    }
    @Override
    public String getNomMaitre() throws RemoteException{
        return nomMaitre;
    }

    @Override
    public String getRace() throws RemoteException{
        return race;
    }

    @Override
    public Espece getEspece() throws RemoteException {
        // Point de controle B : on trace l'identite de l'objet cote serveur.
        System.out.println("[serveur] getEspece(" + nom + ") -> identityHashCode="
                + System.identityHashCode(espece) + ", valeur=" + espece);
        return espece;
    }

    @Override
    public DossierSuivi getDossier() throws RemoteException{
        return dossier;
    }
}
