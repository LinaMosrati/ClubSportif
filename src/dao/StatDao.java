package dao;

import config.DBConnection;
import model.StatMembre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatDao {

    public List<StatMembre>
    getMembresActifs(
            String debut,
            String fin
    ) {

        List<StatMembre> liste =
                new ArrayList<>();

        String sql = """

        SELECT

        CONCAT(
            m.nom,
            ' ',
            m.prenom
        ) nom_complet,

        TIMESTAMPDIFF(
            YEAR,
            m.date_naissance,
            CURDATE()
        ) age,

        COUNT(ai.id)
        total

        FROM archive_inscriptions ai

        JOIN membres m

        ON ai.membre_id=m.id

        WHERE

        ai.statut='ACCEPTEE'

        AND

        DATE(
            ai.date_archivage
        )

        BETWEEN ? AND ?

        GROUP BY

        m.id

        ORDER BY

        total DESC

        """;

        try {

            Connection conn =
                    DBConnection
                    .getConnection();

            PreparedStatement ps =
                    conn
                    .prepareStatement(
                            sql
                    );

            ps.setString(
                    1,
                    debut
            );

            ps.setString(
                    2,
                    fin
            );

            ResultSet rs =
                    ps.executeQuery();

            while (
                    rs.next()
            ) {

                liste.add(

                        new StatMembre(

                                rs.getString(
                                        "nom_complet"
                                ),

                                rs.getInt(
                                        "age"
                                ),

                                rs.getInt(
                                        "total"
                                )

                        )

                );

            }

        }

        catch (
                Exception e
        ) {

            e.printStackTrace();

        }

        return liste;

    }

}