package controller;

import dao.MembreDao;
import model.Membre;
import util.ValidationUtil;

public class MembreController {

    private MembreDao membreDao = new MembreDao();

    public String ajouterMembre(
            String login,
            String motDePasse,
            String nom,
            String prenom,
            String dateNaissance,
            String adresse,
            String telephone,
            String email,
            String poidsText
    ) {

        if (
                ValidationUtil.estVide(login)
                || ValidationUtil.estVide(motDePasse)
                || ValidationUtil.estVide(nom)
                || ValidationUtil.estVide(prenom)
                || ValidationUtil.estVide(dateNaissance)
                || ValidationUtil.estVide(adresse)
                || ValidationUtil.estVide(telephone)
                || ValidationUtil.estVide(email)
                || ValidationUtil.estVide(poidsText)
        ) {
            return "Tous les champs sont obligatoires ❌";
        }

        if (!ValidationUtil.motDePasseValide(motDePasse)) {
            return "Mot de passe minimum 4 caractères ❌";
        }

        if (!ValidationUtil.nomValide(nom)) {
            return "Nom invalide ❌";
        }

        if (!ValidationUtil.nomValide(prenom)) {
            return "Prénom invalide ❌";
        }

        if (!ValidationUtil.telephoneValide(telephone)) {
            return "Téléphone invalide : 8 chiffres ❌";
        }

        if (!ValidationUtil.emailValide(email)) {
            return "Email invalide ❌";
        }

        if (!ValidationUtil.poidsValide(poidsText)) {
            return "Poids invalide ❌";
        }

        double poids = Double.parseDouble(poidsText);

        Membre membre = new Membre(
                0,
                login,
                motDePasse,
                nom,
                prenom,
                dateNaissance,
                adresse,
                telephone,
                email,
                poids
        );

        try {

            boolean success =
                    membreDao.ajouterMembre(
                            membre
                    );

            if (success) {

                return "Membre ajouté avec succès ✅";
            }

        } catch (RuntimeException e) {

            if (
                    e.getMessage().equals(
                            "LOGIN_EXISTE"
                    )
            ) {

                return "Ce login existe déjà ❌";
            }

            return "Erreur base de données ❌";
        }

        return "Erreur inconnue ❌";
    }
    
    
    
    public java.util.List<Membre> getAllMembres() {
        return membreDao.getAllMembres();
    }

    public boolean supprimerMembre(int id, String login) {
        return membreDao.supprimerMembre(id, login);
    }

    public String modifierMembre(Membre membre) {

        if (
                ValidationUtil.estVide(membre.getNom())
                || ValidationUtil.estVide(membre.getPrenom())
                || ValidationUtil.estVide(membre.getDateNaissance())
                || ValidationUtil.estVide(membre.getAdresse())
                || ValidationUtil.estVide(membre.getTelephone())
                || ValidationUtil.estVide(membre.getEmail())
        ) {
            return "Tous les champs sont obligatoires ❌";
        }

        if (!ValidationUtil.nomValide(membre.getNom())) {
            return "Nom invalide ❌";
        }

        if (!ValidationUtil.nomValide(membre.getPrenom())) {
            return "Prénom invalide ❌";
        }

        if (!ValidationUtil.telephoneValide(membre.getTelephone())) {
            return "Téléphone invalide ❌";
        }

        if (!ValidationUtil.emailValide(membre.getEmail())) {
            return "Email invalide ❌";
        }

        if (!ValidationUtil.poidsValide(String.valueOf(membre.getPoids()))) {
            return "Poids invalide ❌";
        }

        boolean success = membreDao.modifierMembre(membre);

        if (success) {
            return "Membre modifié avec succès ✅";
        }

        return "Erreur modification ❌";
    }
}