package controller;

import dao.ActiviteDao;
import model.Activite;
import util.ValidationUtil;

import java.util.List;

public class ActiviteController {

    private ActiviteDao activiteDao = new ActiviteDao();
    public String ajouterActivite(
            String nom,
            String description,
            String capaciteText,
            String jour,
            String horaire
    ) {

        if (
                ValidationUtil.estVide(nom)
                || ValidationUtil.estVide(description)
                || ValidationUtil.estVide(capaciteText)
                || ValidationUtil.estVide(jour)
                || ValidationUtil.estVide(horaire)
        ) {
            return "Tous les champs sont obligatoires ❌";
        }

        int capacite;

        try {
            capacite = Integer.parseInt(capaciteText);
        } catch (Exception e) {
            return "La capacité doit être un nombre ❌";
        }

        if (capacite <= 0) {
            return "La capacité doit être positive ❌";
        }

        Activite activite =
                new Activite(
                        0,
                        nom,
                        description,
                        capacite,
                        jour,
                        horaire,
                        0
                );

        if (activiteDao.ajouterActivite(activite)) {
            return "Activité ajoutée avec succès ✅";
        }

        return "Erreur lors de l'ajout ❌";
    }

    public List<Activite> getAllActivites() {
        return activiteDao.getAllActivites();
    }

    public boolean supprimerActivite(int id) {
        return activiteDao.supprimerActivite(id);
    }

    public String modifierActivite(Activite activite) {

        if (
                ValidationUtil.estVide(activite.getNom())
                || ValidationUtil.estVide(activite.getDescription())
                || ValidationUtil.estVide(activite.getHoraire())
        ) {
            return "Tous les champs sont obligatoires ❌";
        }

        if (activite.getCapaciteMax() <= 0) {
            return "Capacité invalide ❌";
        }

        if (activiteDao.modifierActivite(activite)) {
            return "Activité modifiée avec succès ✅";
        }

        return "Erreur modification ❌";
    }
}