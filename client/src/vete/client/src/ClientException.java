package vete.client.src;

// Exception propre au client : un message lisible, sans aucun type java.rmi visible par l'interface
public class ClientException extends Exception {

    public ClientException(String message) {
        super(message);
    }
}