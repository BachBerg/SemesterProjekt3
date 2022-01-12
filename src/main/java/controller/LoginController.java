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
            // sql kald der kontrollere om brugeren eksitere og returnere brugeroplysninger
            User nyUser = SQL.getSqlOBJ().getUserObjekt(loginData.getUsername());

            // kontrol af login og generer token
            if (hashControl(loginData.getPassword(), nyUser.getPassword())) {
                return JWTHandler.generateJwtToken(nyUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new WebApplicationException(401);
    }

    // main metode til manuelt at lave nye kodeord
    /*public static void main(String[] args) {
        String nytKodeord = "Sundperson123";
        String kodeord = generateHash(nytKodeord);
        System.out.println(kodeord);
        System.out.println(hashControl(nytKodeord, kodeord));
    }*/



    /* metode der blev brugt til at hashe kodeordene */
    public static String generateHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean hashControl(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

