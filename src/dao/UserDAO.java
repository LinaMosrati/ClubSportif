package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.DBConnection;
import model.User;

public class UserDAO {

    public User verifierConnexion(String login, String motDePasse) {

    	String sql =
    		    "SELECT role, premiere_connexion " +
    		    "FROM users " +
    		    "WHERE login = ? " +
    		    "AND mot_de_passe = ?";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, motDePasse);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new User(

                        rs.getString("role"),

                        rs.getBoolean(
                                "premiere_connexion"
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public boolean verifierCode(

    		String login,

    		String code

    		){

    		try{

    		Connection c=
    		DBConnection
    		.getConnection();

    		PreparedStatement ps=
    		c.prepareStatement(

    		"""
    		UPDATE users

    		SET
    		email_verifie=1

    		WHERE

    		login=?

    		AND

    		code_verification=?
    		"""
    		);

    		ps.setString(
    		1,
    		login
    		);

    		ps.setString(
    		2,
    		code
    		);

    		return
    		ps.executeUpdate()>0;

    		}

    		catch(Exception e){

    		return false;

    		}

    		}
    
    public boolean changerMotDePasse(String login, String nouveauMotDePasse) {

        String sql =
                "UPDATE users SET mot_de_passe = ?, premiere_connexion = FALSE WHERE login = ?";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nouveauMotDePasse);
            ps.setString(2, login);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}