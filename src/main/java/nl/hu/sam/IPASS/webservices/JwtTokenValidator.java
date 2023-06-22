package nl.hu.sam.IPASS.webservices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nl.hu.sam.IPASS.model.User;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Optional;

public class JwtTokenValidator {
    private static final String SECRET_KEY = "your-secret-key"; // Replace with your secret key

    private static SecretKey generateSecretKey() {
        byte[] decodedKey = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public static boolean validateToken(String token) {
        try {
            SecretKey secretKey = generateSecretKey();
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // Additional checks if necessary

            return true;
        } catch (Exception e) {
            // Token validation failed
            return false;
        }
    }

    public static Optional<User> extractUserFromToken(String token) {
        try {
            SecretKey secretKey = generateSecretKey();
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            String username = claims.getBody().get("username", String.class); // Modify based on your token payload

            // Search for the user in your list of allUsers using the username
            Optional<User> user = User.getAllUsers().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();

            return user;
        } catch (Exception e) {
            // Token validation failed or user not found
            return Optional.empty();
        }
    }
}