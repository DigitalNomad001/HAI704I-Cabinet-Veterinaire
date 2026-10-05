package vete.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vete.client.src.CabinetService;
import vete.client.cli.CLI;
import vete.common.Cabinet;

// POINT D'ENTRÉE : lit (hôte, port), obtient le stub du cabinet, lance l'interface.
// Usage : java vete.client.ClientMain [hote] [port]    (défaut : localhost 1099)
public class ClientMain {

    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "localhost";
        int port;
        try {
            port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        } catch (NumberFormatException e) {
            System.err.println("Port invalide : " + args[1]);
            return;
        }

        Cabinet cabinet;
        try {
            Registry registry = LocateRegistry.getRegistry(hote, port);
            cabinet = (Cabinet) registry.lookup("Cabinet");
        } catch (NotBoundException e) {
            System.err.println("Le registre ne contient pas \"Cabinet\". Le serveur est-il lancé ?");
            return;
        } catch (RemoteException e) {
            System.err.println("Registre injoignable sur " + hote + ":" + port
                    + ". Vérifiez que le serveur est démarré.");
            return;
        }

        new CLI(new CabinetService(cabinet)).lancer();
    }
}