package controller;

import dataAccesLayer.SQL;
import model.LoginData;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import javax.ws.rs.WebApplicationException;
import java.sql.SQLException;

public class LoginController {

    private LoginController() {
    }

    static private final LoginController loginControllerOBJ = new LoginController();

    static public LoginController getLoginControllerOBJ() {
        return loginControllerOBJ;
    }

    public String doLogin(LoginData loginData) {
        try {
            // sql kald der kontrollere om brugeren eksitere
            String brugerListe = SQL.getSqlOBJ().hentBrugerListe(loginData.getUsername());
            String[] opdelt = brugerListe.split("\\|");

            // kontrol af login og generer token
            if (hashControl(loginData.getPassword(), opdelt[1])) {
                User user = new User(loginData);
                return JWTHandler.generateJwtToken(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new WebApplicationException(401);
    }

    /* metode der blev brugt til at hashe kodeordene */
    public static String generateHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean hashControl(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

