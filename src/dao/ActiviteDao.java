package dao;

import config.DBConnection;
import model.Activite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActiviteDao {

    public boolean ajouterActivite(Activite activite) {

    	String sql =
    			"INSERT INTO activites (nom,description,capacite_max,jour,horaire) VALUES (?,?,?,?,?)";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, activite.getNom());
            ps.setString(2, activite.getDescription());
            ps.setInt(3, activite.getCapaciteMax());
            ps.setString(4,activite.getJour());
            ps.setString(5, activite.getHoraire());
           

           

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Activite> getAllActivites() {

        List<Activite> activites = new ArrayList<>();

        String sql = "SELECT * FROM activites";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Activite activite =
                        new Activite(
                                rs.getInt("id"),
                                rs.getString("nom"),
                                rs.getString("description"),
                                rs.getInt("capacite_max"),
                                rs.getString("jour"),
                                rs.getString("horaire"),
                                0
                        );

                activites.add(activite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activites;
    }

    public boolean supprimerActivite(int id) {

        String sql = "DELETE FROM activites WHERE id = ?";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierActivite(Activite activite) {

        String sql = """
            UPDATE activites
            SET nom = ?, description = ?, capacite_max = ?, jour = ?, horaire = ?
            WHERE id = ?
        """;

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, activite.getNom());
            ps.setString(2, activite.getDescription());
            ps.setInt(3, activite.getCapaciteMax());
            ps.setString(4, activite.getJour());
            ps.setString(5, activite.getHoraire());
            ps.setInt(6, activite.getId());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}