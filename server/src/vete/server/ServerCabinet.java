package vete.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vete.common.Espece;

public class ServerCabinet {

    public static void main(String[] args) {
        try {
            //test A4
            CabinetImpl cabinet = new CabinetImpl();

            cabinet.ajouter(new AnimalImpl("soprano", "Babane", "Europeen", new Espece("Chat", 15)));
            cabinet.ajouter(new AnimalImpl("Lassy", "Hao", "Chien de chasse", new Espece("Chien", 13)));
            cabinet.ajouter(new AnimalImpl("griffith", "guts", "White falcon", new Espece("Falcon", 50)));


            //test A6
            for (int i = 1; i <= 95; i++) {   // 3 + 95 = 98 patients
                cabinet.ajouter(new AnimalImpl("Patient" + i, "Proprio" + i, "Inconnue", new Espece("Chat", 15)));
            }



            Registry registry = LocateRegistry.createRegistry(1099);

            // A4 : le cabinet est le seul objet publié
            registry.rebind("Cabinet", cabinet);

            System.out.println("Serveur prêt : \"Cabinet\" publié sur le port 1099");
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e);
            e.printStackTrace();
        }
    }
}