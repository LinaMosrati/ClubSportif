package model;

public class User {

    private String role;
    private boolean premiereConnexion;
    private boolean emailVerifie;

    public User(
            String role,
            boolean premiereConnexion
    ) {

        this.role = role;
        this.premiereConnexion = premiereConnexion;
    }

    public String getRole() {
        return role;
    }

    public boolean isPremiereConnexion() {
        return premiereConnexion;
    }
    public boolean isEmailVerifie() {
        return emailVerifie;
    }
}