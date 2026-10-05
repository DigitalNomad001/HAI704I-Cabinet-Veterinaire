package vete.common;


 // Exception pour les patients inconnu dans le maps
public class PatientInconnuException extends Exception {


    public PatientInconnuException(String nom) {
        super("Aucun patient nomme \"" + nom + "\"");
    }
}
