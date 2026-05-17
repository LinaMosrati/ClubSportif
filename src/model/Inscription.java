package model;

public class Inscription {

    private int id;
    private Membre membre;
    private Activite activite;
    private String statut;

    public Inscription(int id, Membre membre, Activite activite, String statut) {
        this.id = id;
        this.membre = membre;
        this.activite = activite;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public Membre getMembre() {
        return membre;
    }

    public Activite getActivite() {
        return activite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}