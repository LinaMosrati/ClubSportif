package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.DBConnection;
import model.Membre;

public class MembreDao {

    public boolean ajouterMembre(Membre membre) {

        String sqlMembre = """
            INSERT INTO membres
            (login, mot_de_passe, nom, prenom, date_naissance, adresse, telephone, email, poids)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlUser = """
        	    INSERT INTO users(
        	        login,
        	        mot_de_passe,
        	        role,
        	        premiere_connexion
        	    )
        	    VALUES (?, ?, 'MEMBRE', TRUE)
        	""";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, membre.getLogin());
            psUser.setString(2, membre.getMotDePasse());
            psUser.executeUpdate();

            PreparedStatement psMembre = conn.prepareStatement(sqlMembre);
            psMembre.setString(1, membre.getLogin());
            psMembre.setString(2, membre.getMotDePasse());
            psMembre.setString(3, membre.getNom());
            psMembre.setString(4, membre.getPrenom());
            psMembre.setString(5, membre.getDateNaissance());
            psMembre.setString(6, membre.getAdresse());
            psMembre.setString(7, membre.getTelephone());
            psMembre.setString(8, membre.getEmail());
            psMembre.setDouble(9, membre.getPoids());

            psMembre.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            if (
                    e.getMessage().contains("Duplicate")
                    || e.getMessage().contains("login")
            ) {

                throw new RuntimeException(
                        "LOGIN_EXISTE"
                );
            }

            throw new RuntimeException(
                    "ERREUR_BDD"
            );
        }
    }
}