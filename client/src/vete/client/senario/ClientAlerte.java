package vete.client.senario;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import vete.common.Cabinet;
import vete.common.Espece;
import vete.client.src.ObservateurImpl;

public class ClientAlerte {

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        Cabinet cabinet = (Cabinet) registry.lookup("Cabinet");

        ObservateurImpl obs = new ObservateurImpl(message -> System.out.println("\n>>> ALERTE : " + message));
        cabinet.abonner(obs);
        System.out.println("Abonné. Patients : " + cabinet.getNombrePatients());
        System.out.println("Commandes : '+ nom' ajouter, '- nom' supprimer, 'q' quitter");

        Scanner sc = new Scanner(System.in);
        while (true) {
            String ligne = sc.nextLine().trim();
            try {
                if (ligne.equals("q")) {
                    break;
                } else if (ligne.startsWith("+ ")) {
                    cabinet.ajouterPatient(ligne.substring(2), "Test", "Test", new Espece("Chat", 15));
                    System.out.println("Ajouté. Patients : " + cabinet.getNombrePatients());
                } else if (ligne.startsWith("- ")) {
                    cabinet.supprimerPatient(ligne.substring(2));
                    System.out.println("Supprimé. Patients : " + cabinet.getNombrePatients());
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }

        cabinet.desabonner(obs);
        UnicastRemoteObject.unexportObject(obs, true);   // sinon la JVM du client ne s'arrête pas
        System.out.println("Au revoir.");
    }
}