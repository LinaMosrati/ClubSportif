package config;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            System.out.println(
                    "Connexion réussie ✅"
            );

        } catch (Exception e) {

            System.out.println(
                    "Erreur connexion ❌"
            );

            e.printStackTrace();
        }
    }
}