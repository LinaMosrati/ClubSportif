package model;

public class Activite {

    private int id;
    private String nom;
    private String description;
    private int capaciteMax;
    private String horaire;

    public Activite(int id, String nom, String description, int capaciteMax, String horaire) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.capaciteMax = capaciteMax;
        this.horaire = horaire;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }
}