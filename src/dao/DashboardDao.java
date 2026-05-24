package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardDao {

    public int getNombreMembres() {
        return count("SELECT COUNT(*) FROM membres");
    }

    public int getNombreActivites() {
        return count("SELECT COUNT(*) FROM activites");
    }

    private int count(String sql) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public java.util.List<model.Activite> getDashboardActivites() {

        java.util.List<model.Activite> activites = new java.util.ArrayList<>();

        String sql = """
        	    SELECT 
        	        a.id,
        	        a.nom,
        	        a.description,
        	        a.capacite_max,
        	        a.jour,
        	        a.horaire,
        	        COUNT(i.id) AS participants
        	    FROM activites a
        	    LEFT JOIN inscriptions i
        	        ON a.id = i.activite_id
        	        AND i.statut = 'ACCEPTEE'
        	    GROUP BY 
        	        a.id,
        	        a.nom,
        	        a.description,
        	        a.capacite_max,
        	        a.jour,
        	        a.horaire
        	""";

        try {
            java.sql.Connection conn = config.DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	activites.add(new model.Activite(
            	        rs.getInt("id"),
            	        rs.getString("nom"),
            	        rs.getString("description"),
            	        rs.getInt("capacite_max"),
            	        rs.getString("jour"),
            	        rs.getString("horaire"),
            	        rs.getInt("participants")
            	));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return activites;
    }
    
    
    public int getTauxRemplissage() {

        String sql = """
            SELECT
                (
                    COUNT(
                        CASE
                        WHEN i.statut = 'ACCEPTEE'
                        THEN 1
                        END
                    ) * 100
                ) / SUM(a.capacite_max)
                AS taux
            FROM activites a
            LEFT JOIN inscriptions i
            ON a.id = i.activite_id
        """;

        try {

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("taux");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}