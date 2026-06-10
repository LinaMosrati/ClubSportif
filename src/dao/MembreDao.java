package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.EmailService;
import java.util.ArrayList;
import java.util.List;

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
premiere_connexion,
email_verifie,
code_verification
)
VALUES(
?,
?,
'MEMBRE',
TRUE,
FALSE,
?
)
        	""";

        try {

            Connection conn =
                    DBConnection.getConnection();

            String code =
                    String.valueOf(

                            (int)

                            (
                                    100000
                                    +
                                    Math.random()
                                    *
                                    900000
                            )
                    );

            PreparedStatement psUser =
                    conn.prepareStatement(
                            sqlUser
                    );

            psUser.setString(
                    1,
                    membre.getLogin()
            );

            psUser.setString(
                    2,
                    membre.getMotDePasse()
            );

            psUser.setString(
                    3,
                    code
            );

            psUser.executeUpdate();

            EmailService.envoyerCode(

                    membre.getEmail(),

                    membre.getLogin(),

                    membre.getMotDePasse(),

                    code
            );

        

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
    
    public java.util.List<Membre> getAllMembres() {

        java.util.List<Membre> membres = new java.util.ArrayList<>();

        String sql = "SELECT * FROM membres";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Membre membre = new Membre(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("mot_de_passe"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_naissance"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getDouble("poids")
                );

                membres.add(membre);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return membres;
    }
    public boolean supprimerMembre(int id, String login) {

        String insertArchive =
                "INSERT INTO membres_archive " +
                "(ancien_id, login, mot_de_passe, nom, prenom, date_naissance, adresse, telephone, email, poids) " +
                "SELECT id, login, mot_de_passe, nom, prenom, date_naissance, adresse, telephone, email, poids " +
                "FROM membres WHERE id = ?";

        String deleteMembre =
                "DELETE FROM membres WHERE id = ?";

        try (Connection cnx = DBConnection.getConnection()) {

            cnx.setAutoCommit(false);

            try (
                    PreparedStatement psArchive = cnx.prepareStatement(insertArchive);
                    PreparedStatement psDelete = cnx.prepareStatement(deleteMembre)
            ) {
                psArchive.setInt(1, id);

                int archived = psArchive.executeUpdate();

                if (archived == 0) {
                    cnx.rollback();
                    return false;
                }

                psDelete.setInt(1, id);
                psDelete.executeUpdate();

                cnx.commit();
                return true;

            } catch (Exception e) {
                cnx.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifierMembre(Membre membre) {

        String sql = """
            UPDATE membres
            SET nom = ?, prenom = ?, date_naissance = ?, adresse = ?,
                telephone = ?, email = ?, poids = ?
            WHERE id = ?
        """;

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getDateNaissance());
            ps.setString(4, membre.getAdresse());
            ps.setString(5, membre.getTelephone());
            ps.setString(6, membre.getEmail());
            ps.setDouble(7, membre.getPoids());
            ps.setInt(8, membre.getId());

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
}