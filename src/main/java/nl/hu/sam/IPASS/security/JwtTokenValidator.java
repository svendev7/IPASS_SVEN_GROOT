package nl.hu.sam.IPASS.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import nl.hu.sam.IPASS.model.User;


import java.util.Optional;

public class JwtTokenValidator {
//    desperate attempt om security te maken, werkt niet :(
    private static final String SECRET_KEY = "123";

    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Optional<User> extractUserFromToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            String username = claims.getBody().get("username", String.class);

            Optional<User> user = User.getAllUsers().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();

            return user;
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}