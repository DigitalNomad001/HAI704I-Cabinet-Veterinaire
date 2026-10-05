package vete.common;

public class PatientExistantException extends Exception {

    public PatientExistantException(String nom) {
        super("Patient : \"" + nom + "déjà existé\"");
    }
}
