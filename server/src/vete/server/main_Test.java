package vete.server;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vete.common.Animal;
import vete.common.Espece;

//Test A1
public class main_Test {
    public static void main(String[] args) {
        try {

            //Test pour A1: Animal soprano = new AnimalImpl("Soprano", "Babane", "Europeen", "Chat");


            //Test pour A2
            Espece ep=new Espece("Chat",99);

            Animal soprano = new AnimalImpl("Soprano", "Babane", "Europeen", ep);


            Registry registry = LocateRegistry.createRegistry(1099);


            registry.rebind("Soprano", soprano);

            System.out.println("Serveur pret : \"Soprano\" publie sur le port 1099");
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e);
            e.printStackTrace();
        }
    }
}
