package vete.common;
import java.io.Serializable;

//pour A2
public class Espece implements Serializable{
    private String nom_espece;
    private double esperanceVieMoyenne; // en annees

    public Espece(String nom_espece, double esperanceVieMoyenne) {
        this.nom_espece = nom_espece;
        this.esperanceVieMoyenne = esperanceVieMoyenne;
    }

    public String getNom_espece() {
        return nom_espece;
    }

    public void setNom_espece(String nom_espece) {
        this.nom_espece = nom_espece;
    }

    public double getEsperanceVieMoyenne() {
        return esperanceVieMoyenne;
    }

    public void setEsperanceVieMoyenne(double esperanceVieMoyenne) {
        this.esperanceVieMoyenne = esperanceVieMoyenne;
    }

    @Override
    public String toString() {
        return nom_espece+ " (esperance de vie :" + esperanceVieMoyenne + " ans)";}
}
