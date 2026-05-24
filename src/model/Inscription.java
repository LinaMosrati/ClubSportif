package model;

public class Inscription {

    private int id;
    private String membreNom;
    private String activiteNom;
    private String statut;
    private String horaire;

    public Inscription(int id, String membreNom, String activiteNom, String statut) {
        this.id = id;
        this.membreNom = membreNom;
        this.activiteNom = activiteNom;
        this.statut = statut;
    }

    public Inscription(int id, String membreNom, String activiteNom, String statut, String horaire) {
        this.id = id;
        this.membreNom = membreNom;
        this.activiteNom = activiteNom;
        this.statut = statut;
        this.horaire = horaire;
    }

    public int getId() {
        return id;
    }

    public String getMembreNom() {
        return membreNom;
    }

    public String getActiviteNom() {
        return activiteNom;
    }

    public String getStatut() {
        return statut;
    }

    public String getHoraire() {
        return horaire;
    }
}