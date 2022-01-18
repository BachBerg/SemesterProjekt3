package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import model.User;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.NotAuthorizedException;
import java.security.Key;
import java.util.Calendar;

public class JWTHandler {
    private static Key key;
    private static final int TOKEN_EXPIRY = 2880; //2 days

    public static String generateJwtToken(User userObjekt) {
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.MINUTE, TOKEN_EXPIRY);
        userObjekt.setPassword("");

        return Jwts.builder()
                .setIssuer("Andreas")
                .claim("user", userObjekt)
                .signWith(SignatureAlgorithm.HS512, getKey())
                .setExpiration(expiry.getTime())
                .compact();
    }

    private static Key getKey() {
        if (key == null) {
            if (System.getenv("JWT_SECRET_KEY") != null && System.getenv("JWT_SECRET_KEY") != "") {
                String string = System.getenv("JWT_SECRET_KEY");
                key = new SecretKeySpec(string.getBytes(), 0, string.length(), "HS512");
            } else {
                key = MacProvider.generateKey(SignatureAlgorithm.HS512);
            }
        }
        return key;
    }
    // Validering af token
    public static String validate(String authentication) {
        if(authentication == null){
            throw new NotAuthorizedException("ingen header");
        }
        String[] tokenArray = authentication.split(" ");
        String token = tokenArray[tokenArray.length - 1];
        User user;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody();
            ObjectMapper mapper = new ObjectMapper();

            user = mapper.convertValue(claims.get("user"), User.class);

            System.out.println("valideret user:  " + user);

        } catch (JwtException e){
            System.out.println(e.getClass() +":  "+ e.getMessage());
            throw new NotAuthorizedException(e.getMessage());
        }
        return user.getAuth();
    }

}