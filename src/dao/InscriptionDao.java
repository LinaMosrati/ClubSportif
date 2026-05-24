package dao;

import config.DBConnection;
import model.Inscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDao {

    public List<Inscription> getAllInscriptions() {

        List<Inscription> inscriptions = new ArrayList<>();

        String sql = """
            SELECT 
                i.id,
                CONCAT(m.nom, ' ', m.prenom) AS membre_nom,
                a.nom AS activite_nom,
                i.statut
            FROM inscriptions i
            JOIN membres m ON i.membre_id = m.id
            JOIN activites a ON i.activite_id = a.id
        """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                inscriptions.add(new Inscription(
                        rs.getInt("id"),
                        rs.getString("membre_nom"),
                        rs.getString("activite_nom"),
                        rs.getString("statut")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inscriptions;
    }

    public boolean changerStatut(int id, String statut) {

        String updateSql =
                "UPDATE inscriptions SET statut = ? WHERE id = ?";

        String archiveSql = """
            INSERT INTO archive_inscriptions
            (inscription_id, membre_id, activite_id, statut, date_archivage)

            SELECT 
                i.id,
                i.membre_id,
                i.activite_id,
                i.statut,
                NOW()

            FROM inscriptions i

            JOIN inscriptions accepted
                ON accepted.id = ?

            JOIN activites a1
                ON i.activite_id = a1.id

            JOIN activites a2
                ON accepted.activite_id = a2.id

            WHERE i.membre_id = accepted.membre_id
            AND i.id <> accepted.id
            AND a1.jour = a2.jour
            AND a1.horaire = a2.horaire
        """;

        String deleteSql = """
            DELETE i
            FROM inscriptions i

            JOIN inscriptions accepted
                ON accepted.id = ?

            JOIN activites a1
                ON i.activite_id = a1.id

            JOIN activites a2
                ON accepted.activite_id = a2.id

            WHERE i.membre_id = accepted.membre_id
            AND i.id <> accepted.id
            AND a1.jour = a2.jour
            AND a1.horaire = a2.horaire
        """;

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement psUpdate = conn.prepareStatement(updateSql);
            psUpdate.setString(1, statut);
            psUpdate.setInt(2, id);

            int updated = psUpdate.executeUpdate();

            if (updated == 0) {
                return false;
            }

            if (statut.equals("ACCEPTEE")) {

                PreparedStatement psArchive = conn.prepareStatement(archiveSql);
                psArchive.setInt(1, id);
                psArchive.executeUpdate();

                PreparedStatement psDelete = conn.prepareStatement(deleteSql);
                psDelete.setInt(1, id);
                psDelete.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerInscription(int id) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            // ====================
            // ARCHIVER
            // ====================

            String archiveSql = """
                INSERT INTO archive_inscriptions
                (
                    inscription_id,
                    membre_id,
                    activite_id,
                    statut,
                    date_archivage
                )

                SELECT
                    id,
                    membre_id,
                    activite_id,
                    statut,
                    NOW()

                FROM inscriptions

                WHERE id = ?
            """;

            PreparedStatement archive =
                    conn.prepareStatement(
                            archiveSql
                    );

            archive.setInt(
                    1,
                    id
            );

            archive.executeUpdate();

            // ====================
            // SUPPRIMER
            // ====================

            String deleteSql =
                    "DELETE FROM inscriptions WHERE id=?";

            PreparedStatement delete =
                    conn.prepareStatement(
                            deleteSql
                    );

            delete.setInt(
                    1,
                    id
            );

            delete.executeUpdate();

            return true;

        }

        catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    
    public java.util.Map<String, Integer> getParticipantsParActivite() {

        java.util.Map<String, Integer> stats =
                new java.util.HashMap<>();

        String sql = """
            SELECT
                a.nom,
                COUNT(i.id) nb
            FROM activites a

            LEFT JOIN inscriptions i
                ON a.id = i.activite_id

            WHERE i.statut='ACCEPTEE'

            GROUP BY a.nom
        """;

        try {

            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                stats.put(
                        rs.getString("nom"),
                        rs.getInt("nb")
                );

            }

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return stats;
    }
    
    public boolean archiverToutesInscriptions() {

        String archiveSql = """
            INSERT INTO archive_inscriptions
            (inscription_id, membre_id, activite_id, statut, date_archivage)
            SELECT id, membre_id, activite_id, statut, NOW()
            FROM inscriptions
        """;

        String deleteSql = "DELETE FROM inscriptions";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement psArchive = conn.prepareStatement(archiveSql);
            psArchive.executeUpdate();

            PreparedStatement psDelete = conn.prepareStatement(deleteSql);
            psDelete.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean inscrireMembre(String login, int activiteId) {

        String sql = """
            INSERT INTO inscriptions(membre_id, activite_id, statut)
            SELECT id, ?, 'EN_ATTENTE'
            FROM membres
            WHERE login = ?
        """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, activiteId);
            ps.setString(2, login);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean dejaInscrit(String login, int activiteId) {

        String sql = """
            SELECT *
            FROM inscriptions i
            JOIN membres m
            ON i.membre_id = m.id
            WHERE m.login = ?
            AND i.activite_id = ?
        """;

        try {

            Connection conn =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, login);
            ps.setInt(2, activiteId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String getStatutInscription(String login, int activiteId) {

        String sql = """
            SELECT i.statut
            FROM inscriptions i
            JOIN membres m ON i.membre_id = m.id
            WHERE m.login = ?
            AND i.activite_id = ?
        """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, login);
            ps.setInt(2, activiteId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("statut");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    
    public java.util.List<Inscription> getInscriptionsByLogin(String login) {

        java.util.List<Inscription> inscriptions = new java.util.ArrayList<>();

        String sql = """
            SELECT 
                i.id,
                a.nom AS activite_nom,
                a.horaire,
                i.statut
            FROM inscriptions i
            JOIN membres m ON i.membre_id = m.id
            JOIN activites a ON i.activite_id = a.id
            WHERE m.login = ?
        """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                inscriptions.add(
                        new Inscription(
                                rs.getInt("id"),
                                "",
                                rs.getString("activite_nom"),
                                rs.getString("statut"),
                                rs.getString("horaire")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inscriptions;
    }
    
    public void archiverInscriptionsPassees() {

        String insertSql = """
            INSERT INTO archive_inscriptions
            (inscription_id, membre_id, activite_id, statut)
            SELECT 
                i.id,
                i.membre_id,
                i.activite_id,
                i.statut
            FROM inscriptions i
            JOIN activites a ON i.activite_id = a.id
            WHERE 
                (
                    a.jour = DAYNAME(CURRENT_DATE())
                )
                AND STR_TO_DATE(SUBSTRING_INDEX(a.horaire, ' à ', 1), '%H:%i') < CURRENT_TIME()
        """;

        String deleteSql = """
            DELETE i
            FROM inscriptions i
            JOIN activites a ON i.activite_id = a.id
            WHERE 
                (
                    a.jour = DAYNAME(CURRENT_DATE())
                )
                AND STR_TO_DATE(SUBSTRING_INDEX(a.horaire, ' à ', 1), '%H:%i') < CURRENT_TIME()
        """;

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps1 = conn.prepareStatement(insertSql);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(deleteSql);
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
