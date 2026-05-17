package controller;

import dao.UserDAO;

public class LoginController {

    private UserDAO userDAO = new UserDAO();

    public String changerMotDePasse(
            String login,
            String nouveauMotDePasse,
            String confirmation
    ) {

        if (
                nouveauMotDePasse == null
                || nouveauMotDePasse.trim().isEmpty()
        ) {

            return "Mot de passe obligatoire ❌";
        }

        if (nouveauMotDePasse.length() < 4) {

            return "Minimum 4 caractères ❌";
        }

        if (
                !nouveauMotDePasse.equals(
                        confirmation
                )
        ) {

            return "Les mots de passe ne correspondent pas ❌";
        }

        boolean success =
                userDAO.changerMotDePasse(
                        login,
                        nouveauMotDePasse
                );

        if (success) {

            return "Mot de passe modifié avec succès ✅";
        }

        return "Erreur lors de la modification ❌";
    }
}