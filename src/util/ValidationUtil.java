package util;
import java.time.LocalDate;
import javafx.scene.control.DateCell;

public class ValidationUtil {

    public static boolean estVide(String valeur) {
        return valeur == null || valeur.trim().isEmpty();
    }

    public static boolean emailValide(String email) {

        if (email == null) {
            return false;
        }

        email = email.trim();

        return email.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        );
    }

    public static boolean telephoneValide(String telephone) {

        if (telephone == null) {
            return false;
        }

        telephone = telephone.trim();

        return telephone.matches("^[234579][0-9]{7}$");
    }

    public static boolean motDePasseValide(String motDePasse) {
        return motDePasse != null && motDePasse.length() >= 4;
    }

    public static boolean poidsValide(String poidsText) {
        try {
            double poids = Double.parseDouble(poidsText);
            return poids > 0 && poids <= 250;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean nomValide(String valeur) {
        return valeur != null && valeur.matches("^[A-Za-zÀ-ÿ\\s'-]+$");
    }
    
    
    
    public static boolean dateValide(LocalDate date) {

        if (date == null) {
            return false;
        }

        LocalDate now = LocalDate.now();

        if (date.isAfter(now)) {
            return false;
        }

        if (date.plusYears(12).isAfter(now)) {
            return false;
        }

        if (date.plusYears(100).isBefore(now)) {
            return false;
        }

        return true;
    }
}