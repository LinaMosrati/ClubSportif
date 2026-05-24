package model;

public class Activite {

    private int id;
    private String nom;
    private String description;
    private int capaciteMax;
    private String horaire;
    private int participants;
    private String jour;

    public Activite(int id, String nom, String description, int capaciteMax, String jour,String horaire, int participants) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.capaciteMax = capaciteMax;
        this.horaire = horaire;
        this.jour=jour;
        this.participants = participants;
    }
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public int getCapaciteMax() { return capaciteMax; }
    public String getHoraire() { return horaire; }

    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }
    public void setHoraire(String horaire) { this.horaire = horaire; }
    public int getParticipants() {
        return participants;
    }
    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }
}