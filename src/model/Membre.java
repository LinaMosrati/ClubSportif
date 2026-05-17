package model;

public class Membre {

    private int id;

    private String login;

    private String motDePasse;

    private String nom;

    private String prenom;

    private String dateNaissance;

    private String adresse;

    private String telephone;

    private String email;

    private double poids;

    public Membre(
            int id,
            String login,
            String motDePasse,
            String nom,
            String prenom,
            String dateNaissance,
            String adresse,
            String telephone,
            String email,
            double poids
    ) {

        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.poids = poids;
    }


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public double getPoids() {
        return poids;
    }

 

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }
}