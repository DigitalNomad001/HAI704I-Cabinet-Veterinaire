package vete.client.senario;

import java.lang.reflect.Proxy;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import vete.common.Animal;
import vete.common.Espece;
import vete.common.DossierSuivi;
import vete.common.Cabinet;
import vete.common.PatientInconnuException;
import vete.common.PatientExistantException;
import vete.common.A7Chien;

import vete.client.src.A7_Chien;


public class client_Test {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {

            Registry registry = LocateRegistry.getRegistry(host,1099);


            //Animal animal =(Animal) registry.lookup("Soprano") ;

            // Teste d'appels sur les fonctions A1

            /*
            System.out.println("le nom de animal est: "+animal.getNom());
            System.out.println("le nom de maitre de animal est :"+animal.getNomMaitre());
            System.out.println("le race de animal est :"+animal.getRace());
            System.out.println("l‘espece de animal est :"+animal.getEspece());
            System.out.println("Etat initial de animal est: "+animal.getDossier().getEtatSante());

             */

           /*
            //Teste d'appels sur les fonctions A2

            //on appels le premier fois le méthod, pour observer le identityHashCode(copie) de client et server
            Espece ep1 = animal.getEspece();
            System.out.println("[client] 1ere lecture : identityHashCode = "
                    + System.identityHashCode(ep1) + ", valeur = " + ep1.getNom_espece());

            //on appels le deuxième fois le méthod, pour observer le identityHashCode(copie) de client et server

           //1.on modifie localment le nom de espece de animal
            ep1.setNom_espece("Tigre");
            System.out.println("[client] apres la changement locale : valeur = " + ep1.getNom_espece());

            Espece ep2 = animal.getEspece();
            System.out.println("[client] 2ieme  lecture : identityHashCode = "
                    + System.identityHashCode(ep2) + ", valeur = " + ep2.getNom_espece());

            */


            //Test pour A3
            /*
            DossierSuivi dossier = animal.getDossier();
            System.out.println("[client] classe du dossier : " + dossier.getClass().getName());
            System.out.println("[client] etat initial : " + animal.getDossier().getEtatSante());

            // on modifier le dossier par la premier observation
            dossier.setEtatSante("en bonne santé");
            dossier.ajouterObservation("poids: 5kg");

            //On reprendre le meme animal et son dossier
            Animal animal2 = (Animal) registry.lookup("Soprano") ;
            DossierSuivi dossier2 = animal2.getDossier();
            System.out.println("[client] etat relu : " + animal2.getDossier().getEtatSante());
            System.out.println("[client] historique relu : " + animal2.getDossier().getHistorique());
            */

            /*
            // verifier le stub， point de controle A1
            System.out.println("classe du stub : " + animal.getClass().getName());
            System.out.println("proxy dynamique : " + Proxy.isProxyClass(animal.getClass()));
            */



            //Point de contrôle A4
            System.out.println("Liaisons du registre : " + Arrays.toString(registry.list()));
            Cabinet cabinet = (Cabinet) registry.lookup("Cabinet");
/*
            // liste des patients
            List<Animal> patients = cabinet.getPatients();
            System.out.println("Patients (" + patients.size() + ") :");
            for (Animal a : patients) {
                System.out.println("  - " + a.getNom() );
            }

            //  recherche réussie
            Animal soprano = cabinet.rechercher("soprano");
            System.out.println("Trouvé : " + soprano.getNom());   // adapte le nom de ta méthode

            // recherche infructueuse
            try {
                cabinet.rechercher("Carrefour");
            } catch (PatientInconnuException e) {
                System.out.println("Recherche infructueuse : " + e.getMessage());
            }

            //A5

            int avant = cabinet.getPatients().size();
            System.out.println("Nombre de patients avant : " + avant);

            try {

                Animal milou = cabinet.ajouterPatient("milou", "Tintin", "bulldog", new Espece("chien",13));
                System.out.println("Ajouté, stub reçu : " + milou.getClass().getName());
            } catch (PatientExistantException e) {
                System.out.println("Ajout refusé : " + e.getMessage());
            }

            int apres = cabinet.getPatients().size();
            System.out.println("Nombre de patients après : " + apres);

// vérification par une interaction DISTANTE : recherche par nom
            Animal retrouve = cabinet.rechercher("milou");
            System.out.println("Recherche \"milou\" : " + retrouve.getNom()
                    + ", espèce : " + retrouve.getEspece().getNom_espece()
                    + ", état du dossier : " + retrouve.getDossier().getEtatSante());   // adapte les noms de tes méthodes

*/
            //test A7
            try {
                //error code:
                // cabinet.ajouterPatient("Medor", "Petit", "Beagle", new A7_Chien());

                //corigié A7
                cabinet.ajouterPatient("Medor", "Petit", "Beagle", new A7Chien());
                System.out.println("Ajout réussi (inattendu)");
            }catch (Exception e){
                // afficher toute la chaîne des causes, jusqu'à la cause profonde
                Throwable t = e;
                while (t != null) {
                    System.out.println("-> " + t.getClass().getName() + " : " + t.getMessage());
                    t = t.getCause();
                }
            }




        } catch (Exception e) {
            System.err.println("Erreur client : " + e);
            e.printStackTrace();
        }
    }

}
